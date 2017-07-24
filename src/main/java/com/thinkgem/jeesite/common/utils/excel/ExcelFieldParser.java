package com.thinkgem.jeesite.common.utils.excel;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.Reflections;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jxli
 * @version 2017/6/2
 * @Description ExcelField注解的解析对象, 使用init进行初始化, 并获取annotationList, 其中记录了需要处理的字段或getter方法与注解对象
 */
public class ExcelFieldParser {

	private static Logger logger = LoggerFactory.getLogger(ExcelFieldParser.class);
	/**
	 * 注解列表（Object[]{ ExcelField, Field/Method }）
	 */
	private List<ExcelFieldNode> annotationList = new ArrayList<ExcelFieldNode>();
	/**
	 * 需要解析的POJO类
	 */
	private Class cls;
	/**
	 * 解析方式(导入,导出),参见ExcelField.type
	 */
	private int type;
	/**
	 * 参见参见ExcelField.groups
	 */
	private int[] groups;

	public ExcelFieldParser(Class cls, int type, int... groups) {
		this.cls = cls;
		this.type = type;
		this.groups = groups;
	}

	/**
	 * @return java.util.List<java.lang.Object[]>
	 * @Description 解析
	 */
	public List<ExcelFieldNode> init() {
		// Get annotation field
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs) {
			ExcelField ef = f.getAnnotation(ExcelField.class);
			parseExcelAnnotation(f, ef);
		}

		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms) {
			ExcelField ef = m.getAnnotation(ExcelField.class);
			parseExcelAnnotation(m, ef);
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<ExcelFieldNode>() {
			public int compare(ExcelFieldNode o1, ExcelFieldNode o2) {
				return new Integer((o1.getExcelField()).sort()).compareTo(
						new Integer((o2.getExcelField()).sort()));
			}
		});
		return annotationList;
	}


	/**
	 * @return java.util.List<java.lang.String>
	 * @Description 获取表头
	 */
	public List<String> getHeaderList() {
		// Initialize
		List<String> headerList = Lists.newArrayList();
		if (annotationList == null) {
			return headerList;
		}
		for (ExcelFieldNode note : annotationList) {
			String t = note.getExcelField().title();
			// 如果是导出，则去掉注释
			if (type == 1) {
				String[] ss = StringUtils.split(t, "**", 2);
				if (ss.length == 2) {
					t = ss[0];
				}
			}
			headerList.add(t);
		}
		return headerList;
	}


	/**
	 * @Description  尝试将字符串转换为目标字段类型并赋值
	 * @param stringVal
	 * @param node
	 * @param entity
	 * @return void
	 */
	public void setStringValue2Field(String stringVal, ExcelFieldNode node, Object entity) throws Exception {
		if (StringUtils.isBlank(stringVal)){
			return;
		}

		ExcelField ef = node.getExcelField();
		Object target = node.getTarget();
		// If is dict type, get dict value
		if (StringUtils.isNotBlank(ef.dictType())) {
			stringVal = DictUtils.getDictValue(stringVal, ef.dictType(), "");
		}
		// Get param type and type cast
		Class<?> valType = Class.class;
		if (target instanceof Field) {
			valType = ((Field) target).getType();
		} else if (target instanceof Method) {
			Method method = ((Method) target);
			if ("get".equals(method.getName().substring(0, 3))) {
				valType = method.getReturnType();
			} else if ("set".equals(method.getName().substring(0, 3))) {
				valType = ((Method) target).getParameterTypes()[0];
			}
		}

		Object val = this.string2Value(stringVal, valType, ef.fieldType());
		// set entity value
		if (target instanceof Field) {
			Reflections.invokeSetter(entity, ((Field) target).getName(), val);
		} else if (target instanceof Method) {
			String mthodName = ((Method) target).getName();
			if ("get".equals(mthodName.substring(0, 3))) {
				mthodName = "set" + StringUtils.substringAfter(mthodName, "get");
			}
			Reflections.invokeMethod(entity, mthodName, new Class[]{valType}, new Object[]{val});
		}
	}

	/**
	 * @return java.lang.String
	 * @Description 获取对应字段或getter方法字符串值
	 */
	public <E> String getFieldStringValue(E entity, ExcelFieldNode node) {
		return this.value2String(this.getTargetValue(entity, node), node.getExcelField().fieldType());
	}

	/**
	 * @return java.lang.Object
	 * @Description 获取对应字段或getter方法值
	 */
	private <E> Object getTargetValue(E entity, ExcelFieldNode note) {
		if (note == null || entity == null) {
			return null;
		}
		Object target = note.getTarget();
		ExcelField ef = note.getExcelField();
		if (null == target || null == ef) {
			return null;
		}

		Object val = null;
		try {
			if (StringUtils.isNotBlank(ef.value())) {
				val = Reflections.invokeGetter(entity, ef.value());
			} else {
				if (target instanceof Field) {
					val = Reflections.invokeGetter(entity, ((Field) target).getName());
				} else if (target instanceof Method) {
					val = Reflections
							.invokeMethod(entity, ((Method) target).getName(), new Class[]{}, new Object[]{});
				}
			}
			// If is dict, get dict label
			if (StringUtils.isNotBlank(ef.dictType())) {
				val = DictUtils.getDictLabel(val == null ? "" : val.toString(), ef.dictType(), "");
			}
		} catch (Exception e1) {
			val = "" ;
			logger.warn("获取值失败", e1);
		}
		return val;
	}
	/**
	 * @return java.lang.String
	 * @Description 将值转为String类型
	 */
	private String value2String(Object val, Class fieldType) {
		String stringVal;
		if (val == null) {
			stringVal = "";
		} else if (val instanceof String) {
			stringVal = (String) val;
		} else if (val instanceof Integer) {
			stringVal = val.toString();
		} else if (val instanceof Long) {
			stringVal = val.toString();
		} else if (val instanceof Double) {
			stringVal = val.toString();
		} else if (val instanceof Float) {
			stringVal = val.toString();
		} else if (val instanceof BigDecimal) {
			stringVal = val.toString();
		} else if (val instanceof Date) {
			stringVal = DateUtils.formatDate((Date) val);
		} else {
			try {
				if (fieldType != Class.class) {
					stringVal = (String) fieldType.getMethod("setValue", Object.class).invoke(null, val);
				} else {
					stringVal = (String) Class
							.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
									"fieldtype." + val.getClass().getSimpleName() + "Type"))
							.getMethod("setValue", Object.class).invoke(null, val);
				}
			} catch (Exception e) {
				logger.warn("值转为String失败", e);
				stringVal = String.valueOf(val);
			}
		}
		return stringVal;
	}

	/**
	 * @Description  将String转换为目标对象
	 * @param stringVal
	 * @param fieldType
	 * @return java.lang.Object
	 */
	private Object string2Value(String stringVal,Class valType,Class fieldType)
			throws Exception {
		Object val;
		if (valType == String.class) {
			if (StringUtils.endsWith(stringVal, ".0")) {
				val = StringUtils.substringBefore(stringVal, ".0");
			} else {
				val = stringVal;
			}
		} else if (valType == Integer.class) {
			val = Double.valueOf(stringVal).intValue();
		} else if (valType == Long.class) {
			val = Double.valueOf(stringVal).longValue();
		} else if (valType == Double.class) {
			val = Double.valueOf(stringVal);
		} else if (valType == Float.class) {
			val = Float.valueOf(stringVal);
		} else if (valType == Date.class) {
			val = DateUtil.getJavaDate(Double.valueOf(stringVal));
		} else {
			if (fieldType != Class.class) {
				val = fieldType.getMethod("getValue", String.class).invoke(null, stringVal);
			} else {
				val = Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
						"fieldtype." + valType.getSimpleName() + "Type")).getMethod("getValue", String.class).invoke(null, stringVal);
			}
		}
		return val;
	}

	/**
	 * @Description  获取注解关系列表
	 * @param target
	 * @param ef
	 * @return void
	 */
	private void parseExcelAnnotation(Object target, ExcelField ef) {
		if (ef != null && (ef.type() == 0 || ef.type() == type)) {
			if (groups != null && groups.length > 0) {
				boolean inGroup = false;
				for (int g : groups) {
					if (inGroup) {
						break;
					}
					for (int efg : ef.groups()) {
						if (g == efg) {
							inGroup = true;
							annotationList.add(new ExcelFieldNode(target, ef));
							break;
						}
					}
				}
			} else {
				annotationList.add(new ExcelFieldNode(target, ef));
			}
		}
	}

	public class ExcelFieldNode {

		private Object target;
		private ExcelField excelField;

		public ExcelFieldNode(Object target,
				ExcelField excelField) {
			this.target = target;
			this.excelField = excelField;
		}

		public Object getTarget() {
			return target;
		}

		public void setTarget(Object target) {
			this.target = target;
		}

		public ExcelField getExcelField() {
			return excelField;
		}

		public void setExcelField(ExcelField excelField) {
			this.excelField = excelField;
		}
	}
}
