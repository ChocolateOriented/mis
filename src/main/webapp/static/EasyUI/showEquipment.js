//---------------------------------------------------- Serverinfo创建弹出页面 -------------------------------------
function addEquipment(cid) {
	$('#serverinfoList_dialog').dialog( {
		title : '新增服务器信息',
		width : 720,
		height : 320,
		align : 'center',
		closed : false,
		cache : false,
		href : "/excloudPlatform/idcinfo/showAddEquipment",
		modal : true,
		onLoad : function() {
			//		$("#serverinfoList_dialog").form('clear');
		var data1 = $('#infoLab_combobox').combobox('getData');
		if (data1.length > 0) {
			$("#infoLab_combobox").combobox('select', cid);
		}
		var data2 = $('#infoServertype_combobox').combobox('getData');
		if (data2.length > 0) {
			$("#infoServertype_combobox").combobox('select', data2[0].svtId);
		}
		var data3 = $('#gsCust_combobox').combobox('getData');
		if (data3.length > 0) {
			$("#gsCust_combobox").combobox('select', data3[0].cusId);
		}
		var data4 = $('#syCust_combobox').combobox('getData');
		if (data4.length > 0) {
			$("#syCust_combobox").combobox('select', data4[0].cusId);
		}
		$("#sevStatus_combobox").combobox('select', 0);
		//					$('#serverinfoList_dialog').dialog('open').dialog('setTitle', '新增服务器信息');
	}
	});
	setTimeout(function() {
		//加载机房
			initLab(cid);
			//加载服务器类型
			initSerType();
			//加载客户
			initCustomer();
			// 保存方法
			saveServerinfo();
		}, 500);
}

//----------------------------------------------------Serverinfo页面加载启动的方法-------------------------------------

function initLab(cid) {
	$("#infoLab_combobox").combobox( {
		url : "../labinfo/allList",
		valueField : "labId",
		textField : "labName",
		panelHeight : "200",
		editable : false, //不允许手动输入
		onLoadError : function() {
			$.messager.alert('错误提示', '数据加载异常,请刷新后重试', 'error');
		},
		onLoadSuccess : function() { //数据加载完毕事件
			var data = $('#infoLab_combobox').combobox('getData');
			if (data.length > 0) {
				$("#infoLab_combobox").combobox('select', cid);
			}
		}
	});
}

function initSerType() {
	$("#infoServertype_combobox").combobox(
			{
				url : "../serverType/allList",
				valueField : "svtId",
				textField : "svtEname",
				panelHeight : "200",
				editable : false, //不允许手动输入
				onLoadError : function() {
					$.messager.alert('错误提示', '数据加载异常,请刷新后重试', 'error');
				},
				onLoadSuccess : function() { //数据加载完毕事件
					var data = $('#infoServertype_combobox')
							.combobox('getData');
					if (data.length > 0) {
						$("#infoServertype_combobox").combobox('select',
								data[0].svtId);
					}
				}
			});
}

function initCustomer() {
	$("#gsCust_combobox").combobox( {
		url : "../customer/allList",
		valueField : "cusId",
		textField : "cusCompany",
		panelHeight : "200",
		editable : false, //不允许手动输入
		onLoadError : function() {
			$.messager.alert('错误提示', '数据加载异常,请刷新后重试', 'error');
		},
		onLoadSuccess : function() { //数据加载完毕事件
			var data = $('#gsCust_combobox').combobox('getData');
			if (data.length > 0) {
				$("#gsCust_combobox").combobox('select', data[0].cusId);
			}
		}
	});
	$("#syCust_combobox").combobox( {
		url : "../customer/allList",
		valueField : "cusId",
		textField : "cusCompany",
		panelHeight : "200",
		editable : false, //不允许手动输入
		onLoadError : function() {
			$.messager.alert('错误提示', '数据加载异常,请刷新后重试', 'error');
		},
		onLoadSuccess : function() { //数据加载完毕事件
			var data = $('#syCust_combobox').combobox('getData');
			if (data.length > 0) {
				$("#syCust_combobox").combobox('select', data[0].cusId);
			}
		}
	});
}

//更新服务器信息
function saveServerinfo() {
	if ($('#serverinfo_form').form('enableValidation').form('validate')) {
		//创建传递的参数
		var postdata = {
			sevId : $("#sevId").val(),
			sevNumber : $("#sevNumber").val(),
			sevAsset : $("#sevAsset").val(),
			sevStatus : $("#sevStatus_combobox").combobox('getValue'),
			sevSize : $("#sevSize").val(),
			sevSystemver : $("#sevSystemver").val(),
			sevWarranty : $("#sevWarranty").val(),
			sevInitialhardware : $("#sevInitialhardware").val(),
			sevMemo : $("#sevMemo").val(),
			sevBrand : $("#sevBrand").val(),
			sevModel : $("#sevModel").val(),
			sevSnmp : $("#sevSnmp").val(),
			sevCabinet : $("#sevCabinet").val(),
			sevCusid : $("#gsCust_combobox").combobox('getValue'),
			sevUsrcusid : $("#syCust_combobox").combobox('getValue'),
			InfoLabID : $("#infoLab_combobox").combobox('getValue'),
			InfoServertypeID : $("#infoServertype_combobox").combobox(
					'getValue')
		};
		//发送异步请求到后台保存用户数据
		$.post("../serverinfo/saveOrUpdate", postdata, function(data) {
			if (data == "OK") {
				$.messager.alert('提示', '操作成功!', 'info');
				//						$("#serverinfoList_table").datagrid("reload");
			} else if (data == "ERROR") {
				$.messager.alert('警告', '操作失败，请您检查!', 'error');
			} else {
				$.messager.alert('警告', data.toString(), 'error');
			}
		});
	}
}


//---------------------------------------------------- Deviceinfo创建弹出页面 ---------------------------------------
function addDeviceinfo(cid) {
	
	$('#device_dialog').dialog( {
		title : '新增交换机信息',
		width : 720,
		height : 360,
		align : 'center',
		closed : false,
		cache : false,
		href : "/excloudPlatform/idcinfo/showAddDeviceInfo",
		modal : true,
		onLoad : function() {
//			$("#device_form").form('clear');
			var data = $('#labInfo').combobox('getData');
			if (data.length > 0) {
				$("#labInfo").combobox('select', cid);
			}
		}
	});
	
	setTimeout(function() {
		//加载机房
			initLabInfo(cid);
		}, 500);
}


//----------------------------------------------------Deviceinfo页面加载启动的方法-------------------------------------

function initLabInfo(cid) {
	$("#labInfo").combobox( {
		url : "../labinfo/allList",
		valueField : "labId",
		textField : "labName",
		panelHeight : "200",
		editable : false, //不允许手动输入
		onLoadError : function() {
			$.messager.alert('错误提示', '数据加载异常,请刷新后重试', 'error');
		},
		onLoadSuccess : function() { //数据加载完毕事件
			var data = $('#labInfo').combobox('getData');
			if (data.length > 0) {
				$("#labInfo").combobox('select', cid);
			}
		}
	});
}

//添加或更新信息
	function saveOrUpdateInfo(){
		if($('#device_form').form('enableValidation').form('validate')){
			//创建传递的参数
			var postdata = {
				devId : $('#devId').val(),
		 		devNumber : $('#devNumber').val(),
				devAsset : $('#devAsset').val(),
				devSnmp : $('#devSnmp').val(),
				devIp : $('#devIp').val(),
				devWarranty : $('#devWarranty').val(),
				devBrand : $('#devBrand').val(),
				devModel : $('#devModel').val(),
				devCabinet : $('#devCabinet').val(),
				devMemo : $('#devMemo').val(),
				devInitialhardware : $('#devInitialhardware').val(),
				devManagementip : $('#devManagementip').val(),
				infoLabInt : $("#labInfo").combobox('getValue')
			};
			//发送异步请求到后台保存用户数据
			$.post("../device/update", postdata, function(data) {
				if (data == "OK") {
					$.messager.alert('提示', '操作成功!', 'info');
					$("#deviceList_table").datagrid("reload");
				} else if(data == "ERROR"){
					$.messager.alert('警告', '操作失败，请您检查!', 'error');
				} else{
					$.messager.alert('警告', data.toString(), 'error');
				}
			});
		}
	}
