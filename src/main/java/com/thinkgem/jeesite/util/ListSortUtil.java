package com.thinkgem.jeesite.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *  List按照指定字段排序工具类
 */

public class ListSortUtil{
	public static final String SORT_MODE_DESC = "desc";//降序

	/**
	 * @param targetList 目标排序List
	 * @param sortField 排序字段(实体类属性名)
	 * @param sortMode 排序方式（asc or  desc）
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> void sort(List<T> targetList, final String sortField, final String sortMode) {

		Collections.sort(targetList, new Comparator() {
			public int compare(Object obj1, Object obj2) { 
				int retVal = 0;
				try {
					//首字母转大写
					String newStr=sortField.substring(0, 1).toUpperCase()+sortField.replaceFirst("\\w",""); 
					String methodStr="get"+newStr;
					
					Method method1 = ((T)obj1).getClass().getMethod(methodStr, null);
					Method method2 = ((T)obj2).getClass().getMethod(methodStr, null);
					if (sortMode != null && "desc".equals(sortMode)) {
						retVal = method2.invoke(((T) obj2), null).toString().compareTo(method1.invoke(((T) obj1), null).toString()); // 倒序
					} else {
						retVal = method1.invoke(((T) obj1), null).toString().compareTo(method2.invoke(((T) obj2), null).toString()); // 正序
					}
				} catch (Exception e) {
					throw new RuntimeException();
				}
				return retVal;
			}
		});
	}
	
	
	
//	public static void main(String[] args) {  
//        Map<String, Integer> map = new HashMap<String, Integer>();  
//        map.put("1", 2);  
//        map.put("5", 1);  
//        map.put("20", 4);  
//        map.put("30", 3);  
//        List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());  
//  
//        // 对HashMap中的key 进行排序  
//        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {  
//            public int compare(Map.Entry<String, Integer> o1,  
//                    Map.Entry<String, Integer> o2) {  
////              System.out.println(o1.getKey()+"   ===  "+o2.getKey());  
//                return (o1.getKey()).toString().compareTo(o2.getKey().toString());  
//            }  
//        });  
//        // 对HashMap中的key 进行排序后  显示排序结果  
//        for (int i = 0; i < infoIds.size(); i++) {  
//            String id = infoIds.get(i).toString();  
//            System.out.print(id + "  ");  
//        }  
//          
//        System.out.println();  
//          
//        // 对HashMap中的 value 进行排序  
//        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {  
//            public int compare(Map.Entry<String, Integer> o1,  
//                    Map.Entry<String, Integer> o2) {  
//                return (o1.getValue()).toString().compareTo(o2.getValue().toString());  
//            }  
//        });  
//  
//        // 对HashMap中的 value 进行排序后  显示排序结果  
//        for (int i = 0; i < infoIds.size(); i++) {  
//            String id = infoIds.get(i).toString();  
//            System.out.print(id + "  ");  
//        }  
//  
//    }  
	
	
	/**
     * 冒泡排序
     * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。  
     * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。  
     * 针对所有的元素重复以上的步骤，除了最后一个。
     * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。 
     * @param numbers 需要排序的整型数组
     */
    public static void bubbleSort(int[] numbers)
    {
        int temp = 0;
        int size = numbers.length;
        for(int i = 0 ; i < size-1; i ++)
        	{
        	for(int j = 0 ;j < size-1-i ; j++)
		        {
		            if(numbers[j] > numbers[j+1])  //交换两数位置
		            {
		            temp = numbers[j];
		            numbers[j] = numbers[j+1];
		            numbers[j+1] = temp;
		            }
		        }
        	}
    }
    
    
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(76);
		list.add(4);
		list.add(786);
		list.add(43);
		list.add(21);
		list.add(432);
		list.add(10);
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 1; j < list.size() - i; j++) {
				Integer a;
				if ((list.get(j - 1)).compareTo(list.get(j)) > 0) { // 比较两个整数的大小

					a = list.get(j - 1);
					list.set((j - 1), list.get(j));
					list.set(j, a);
				}
			}
		}
		for (Integer s : list) {
			System.out.println(s.intValue());
		}
	}
    
}
