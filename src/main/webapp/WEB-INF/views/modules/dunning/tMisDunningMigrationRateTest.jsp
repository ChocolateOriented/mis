<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>迁徙率测试</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	$("#migration1").click(function(){
		$.ajax({
			url:"${ctx}/dunning/tMisMigrationRateReport/autoInsertTmpMoveCycleDB",
			type:"GET",
			data:{},
			success:function(data){
					top.$.jBox.tip("完成");
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
                   alert("查询失败:"+textStatus);
                }
			
		});
	});
	
	$("#migration2").click(function(){
		$.ajax({
			url:"${ctx}/dunning/tMisMigrationRateReport/autoMigrationRateGetData",
			type:"GET",
			data:{},
			success:function(data){
					top.$.jBox.tip("完成");
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
                   alert("查询失败:"+textStatus);
                }
			
		});
	});
	
	$("#migration3").click(function(){
		if(!$("#margTime").val()){
			$("#tip").html("时间不能为空");
			return ;
		}
		$("#tip").html("");
		$.ajax({
			url:"${ctx}/dunning/tMisMigrationRateReport/autoInsertMigrationRateReportDB?yesterday="+$("#margTime").val(),
			type:"GET",
			data:{},
			success:function(data){
					top.$.jBox.tip("完成");
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
                   alert("查询失败:"+textStatus);
                }
			
		});
	});
	
	$("#migration4").click(function(){
		$.ajax({
			url:"${ctx}/dunning/tMisMigrationRateReport/autoInsertHistoryMigrationRateReportDB/1",
			type:"GET",
			data:{},
			success:function(data){
					top.$.jBox.tip("完成");
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
                   alert("查询失败:"+textStatus);
                }
			
		});
	});
	
	$("#migrationDelete").click(function(){
		$.ajax({
			url:"${ctx}/dunning/tMisMigrationRateReport/migrationDelete/1",
			type:"GET",
			data:{},
			success:function(data){
				if(data=="Ok"){
					alert("删除成功");
					
				}
				
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
                   alert("删除失败:"+textStatus);
                }
			
		});
	});

    $("#migration5").click(function(){
        $.ajax({
            url:"${ctx}/dunning/tMisMigrationRateReport/migration5/1",
            type:"GET",
            data:{},
            success:function(data){
                if(data=="Ok"){
                    alert("发送成功");

                }

            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                alert("发送失败:"+textStatus);
            }

        });
    });

    $("#migration6").click(function(){
        $.ajax({
            url:"${ctx}/dunning/tMisMigrationRateReport/autoInsertHistoryMigrationRateReportDB/2",
            type:"GET",
            data:{},
            success:function(data){
                top.$.jBox.tip("完成");
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                alert("查询失败:"+textStatus);
            }

        });
    });

    $("#migrationMemberDelete").click(function(){
        $.ajax({
            url:"${ctx}/dunning/tMisMigrationRateReport/migrationDelete/2",
            type:"GET",
            data:{},
            success:function(data){
                if(data=="Ok"){
                    alert("删除成功");

                }

            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                alert("删除失败:"+textStatus);
            }

        });
    });

    $("#migration7").click(function(){
        $.ajax({
            url:"${ctx}/dunning/tMisMigrationRateReport/migration5/2",
            type:"GET",
            data:{},
            success:function(data){
                if(data=="Ok"){
                    alert("发送成功");

                }

            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
                alert("发送失败:"+textStatus);
            }

        });
    });
});

</script>
</head>
<body>
	<input id="migration1" class="btn btn-primary" type="button" value="插入cycyle" />
	<div>----------------------------------</div>
	<%--<input id="migration2" class="btn btn-primary" type="button" value="更新迁徙基础表" />--%>
	<li>
		<label>获取数据的时间</label>
		<input id="margTime" name="margTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
		onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> 
		<span ><font color="red" id="tip"></font></span>
	</li>
	<input id="migration3" class="btn btn-primary" type="button" value="计算并保存迁徙率报表" />
	<table>
		<tr>
			<td><input id="migration4" class="btn btn-primary" type="button" value="迁徙率历史数据" /></td>
			<td><input id="migrationDelete" class="btn btn-primary" type="button" value="删除迁徙率数据" /></td>
			<td><input id="migration5" class="btn btn-primary" type="button" value="手动发送迁徙表邮件" /></td>
		</tr>
		<tr>
			<td><input id="migration6" class="btn btn-primary" type="button" value="会员卡迁徙率历史数据" /></td>
			<td><input id="migrationMemberDelete" class="btn btn-primary" type="button" value="删除会员卡迁徙率数据" /></td>
			<td><input id="migration7" class="btn btn-primary" type="button" value="手动发送会员卡迁徙表邮件" /></td>
		</tr>
	</table>

</body>
</html>