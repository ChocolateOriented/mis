//----------------------------显示客户信息---------------------------------------------
function showcusinfo(cid) {
			
$('#showcusinfo').dialog({
    title: 'cusinfo',
    width: 750,
    height: 400,
    closed: false,
    cache: false,
    href: '/excloudPlatform/showcusinfo.jsp',
    modal: true,
    onLoad:function(){
	
	
	$("#customerupdate_dialog").form('clear');
	
//--------------------------------下拉框选项---------------------------------------------------
//		$.post("/excloudPlatform/bandwidthdb/allTypeList",{type:6}, function(data) {
//			$("#bwdb_combobox").combobox( {
//				data : data,
//				valueField : "ddiId",
//				textField : "ddiName",
//				panelHeight :"auto",
//				editable : false, //不允许手动输入
//				onLoadError : function() {
//					window.location.href = "error.jsp";
//				},
//				onLoadSuccess : function() { //数据加载完毕事件
//					if (data.length > 0) {
//						$("#bwdb_combobox").combobox('select',data[0].ddiId);
//					}
//					for(var i=0;i<data.length;i++){
//						if(data[i].ddiDefault=='Y'){
//							pageDBinfo = data[i];
//							break;
//						}
//					}
//				}
//			});
//		});
//		$.post("/excloudPlatform/branch/allList", '', function(data) {
//			$("#branch_combobox").combobox( {
//				data : data,
//				valueField : "bcId",
//				textField : "bcName",
//				panelHeight :"auto",
//				editable : false, //不允许手动输入
//				onLoadError : function() {
//					window.location.href = "error.jsp";
//				},
//				onLoadSuccess : function() { //数据加载完毕事件
//					if (data.length > 0) {
//						$("#branch_combobox").combobox('select', data[0].bcId);
//					}
//				}
//			});
//		});
	
	$.getJSON("/excloudPlatform/customer/findById", {cusId : cid }, function(data) {
					$('#cusId').val(cid);
			 		$('#cusCompany').val(data.cusCompany);
					$('#cusAliasname').val(data.cusAliasname);
					$('#cusNumber').val(data.cusNumber);
					$('#cusLinkman').val(data.cusLinkman);
					$('#cusPhone').val(data.cusPhone);
					$('#cusEmail').val(data.cusEmail);
					$('#cusAddress').val(data.cusAddress);
					$('#cusAssets').val(data.cusAssets);
					$('#cusAsseted').val(data.cusAsseted);
					$('#cusCompanytype').val(data.cusCompanytype);
					$('#cusState').val(data.cusState);
					$('#cusBorn').val(data.cusBorn);
					$('#cusValiddate').val(data.cusValiddate);
					$('#cusRegauthority').val(data.cusRegauthority);
					$('#cusAuthority').val(data.cusAuthority);
					$('#cusScope').val(data.cusScope);
					$('#cusRestartpwd').val(data.cusRestartpwd);
					$('#branch_combobox').val(data.infoBranch.bcName);
					$("#bwdb_combobox").val(data.infoBwdatabase.ddiName);
					$('#cusCreate').val(data.strCusCreate);
					$('#cusDisable').val(data.strCusDisable);
					$('#cusDelete').val(data.strCusDelete);
					
					//不可编辑
	                $('#cusRole').val(data.cusRole);
	                $('#cusStatus').val(data.cusStatus);
					$('#cusParent').val(data.cusParent);
				});
    }
});
			
			}
	



//----------------------------显示机房信息---------------------------------------------
function showlabinfo(lid) {
$('#showlabinfo').dialog({
    title: 'labinfo',
    width: 750,
    height: 400,
    closed: false,
    cache: false,
    href: '/excloudPlatform/showlabinfo.jsp',
    modal: true,
    onLoad:function(){
		$("#labinfo_form").form('clear');
		$.getJSON("/excloudPlatform/labinfo/findById", { labId: lid }, function (data) {
			$('#labId').val(lid);
	 		$('#labName').val(data.labName);
	 		$('#labIp').val(data.labIp);
			$('#labNumber').val(data.labNumber=="null"?"":data.labNumber);
			$("#labState").combobox('select',data.labState);
			$('#labAddress').val(data.labAddress=="null"?"":data.labAddress);
			$('#labIsp').val(data.labIsp=="null"?"":data.labIsp);
			$('#labLabsm').val(data.labLabsm=="null"?"":data.labLabsm);
			$('#labLabsmtel').val(data.labLabsmtel=="null"?"":data.labLabsmtel);
			$('#labConsignee').val(data.labConsignee=="null"?"":data.labConsignee);
			$('#labConsigneetel').val(data.labConsigneetel=="null"?"":data.labConsigneetel);
			$('#labShipaddress').val(data.labShipaddress=="null"?"":data.labShipaddress);
			$('#labResourcemen').val(data.labResourcemen=="null"?"":data.labResourcemen);
			$('#labResourcetel').val(data.labResourcetel=="null"?"":data.labResourcetel);
			$('#labRestartway').val(data.labRestartway=="null"?"":data.labRestartway);
			$('#labRestartpwd').val(data.labRestartpwd=="null"?"":data.labRestartpwd);
			$('#labRestarttel').val(data.labRestarttel=="null"?"":data.labRestarttel);
			$('#labEngineer').val(data.labEngineer=="null"?"":data.labEngineer);
			$('#labEngineertel').val(data.labEngineertel=="null"?"":data.labEngineertel);
		});
		
		
		
		
    }
});
}
	












	
	