<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>手动分案</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        //根据队列,组类型,账号分案状态,组名查询催收人员
        var selectedPeoples = [];
        function selectPeople(obj) {
            var isSelected = $(obj).attr("selected");
            var peopleId = $(obj).attr("peopleId");
            if (isSelected) {
                for (var i = 0; i < selectedPeoples.length; i++) {
                    if (selectedPeoples[i] == peopleId) {
                        selectedPeoples.splice(i, 1);
                        break;
                    }
                }
                $(obj).css("background-color", "white");
                $(obj).css("color", "#555");
                $(obj).attr("selected", false);
            } else {
                $(obj).css("background-color", "#2fa4e7");
                $(obj).css("color", "#fff");
                $(obj).attr("selected", true);
                selectedPeoples.push(peopleId);
            }
        }

        function moveElement(obj, peopleId) {
            var newElem = obj.clone(true);
            newElem.css("background-color", "white");
            newElem.css("color", "#555");
            newElem.children("span").css("display", "inline-block");
            newElem.removeAttr("onclick");
            newElem.prop("id", peopleId);
            newElem.children("input").val(peopleId);
            newElem.children("input").prop("name", "newdunningpeopleids");
            $("#rightContainer").append(newElem);
            var curNum = parseInt($("#selectedDunningPeople").text() || "0");
            $("#selectedDunningPeople").text(curNum + 1);
        }

        function leftMoveToRight() {
            for (var i = 0; i < selectedPeoples.length; i++) {
                var selected = $("#leftContainer div[peopleId='" + selectedPeoples[i] + "']");
                var rightElem = $("#rightContainer div[peopleId='" + selectedPeoples[i] + "']");
                if (rightElem.length > 0) {
                    continue;
                }

                moveElement(selected, selectedPeoples[i]);
            }
        }

        function leftAllMoveToRight() {
            var leftPeoples = $("#leftContainer div");
            if (leftPeoples.length == 0) {
                return;
            }

            leftPeoples.each(function() {
                var elem = $(this)
                var rightElem = $("#rightContainer div[peopleId='" + elem.attr("peopleId") + "']");
                if (rightElem.length > 0) {
                    return;
                }

                moveElement(elem, elem.attr("peopleId"));
            });
        }

        function deletePeople(obj) {
            $(obj).parent().remove();
            var curNum = parseInt($("#selectedDunningPeople").text() || "0");
            if (curNum <= 0) {
                return;
            }
            $("#selectedDunningPeople").text(curNum - 1);
        }

        function getPeople() {
            var cycles = [];
            $("input[name='cycles']:checked").each(function() {
                cycles.push($(this).val());
            })
            var grouptype = [];
            $("input[name='grouptype']:checked").each(function() {
                grouptype.push($(this).val());
            })
            var status = [];
            $("input[name='status']:checked").each(function() {
                status.push($(this).val());
            })
            var bizTypes = [];
            $("input[name='biztype']:checked").each(function() {
            	bizTypes.push($(this).val());
            })
			var bizTypeOther =$("#biztypeValue").val();
            if(bizTypes!=null && bizTypes.length>0){
            	bizTypeOther="";
            }
				
            var name = $("#groupList").val();
            var organizationName = $("#organizationName").val();
            var dunningpeoplename = $("#searchName").val() || "";
            var param = {
            	bizType: bizTypes,
            	bizTypeOther: bizTypeOther,
                dunningcycle: cycles,
                type: grouptype,
                auto: status,
                name: $("#groupList").val(),
                dunningpeoplename: dunningpeoplename,
                organizationName:organizationName
            };
            $.post("${ctx}/dunning/tMisDunningTask/dialogDistributionPeople", param, function(peopleList) {
                $("#leftContainer").empty();
                selectedPeoples = [];
                if (!peopleList) {
                    return;
                }
                for (var i = 0; i < peopleList.length; i++) {
                    addPeople(peopleList[i]);
                }
            });
        }

        function addPeople(people) {
            var templ = $("#template");
            var elem = templ.clone(true);
            elem.attr("peopleId", people.id);
            elem.children("#peopleName").text(people.name);
            elem.css("display", "block");
            $("#leftContainer").append(elem);
        }
        
        $(document).ready(function() {
            check_orders = [];
            var orders = window.parent.document.getElementsByName("orders");
            for(k in orders){
                if(orders[k].checked){
                    check_orders.push(orders[k].id);
                }
            }

            $("#orders").val(check_orders);
            $("#selectedOrders").text(check_orders.length);

            $('#biztypes').change(function() {
                var checked = $('#biztypes').prop("checked");
                $("input[value='"+$("#biztypeValue").val()+"']").attr("disabled",false);
                if (!checked) {
                    $("input[name='biztype']").prop("checked", false);
                    $("input[name='biztype']").prop("disabled", true);
                    getPeople();
                }

            });
            $("input[value='"+$("#biztypeValue").val()+"']").change(function() {
              if($(this).attr("checked")){
                $("input[name='biztype']").attr("disabled", false);
              }else{
                	if($('#biztypes').attr("checked")){
	                	if($("#biztypeValue").val()=="JHJJ"){
	                    	$("#biztype0").attr("checked", false);
	                    	$("#biztype0").attr("disabled", true);
	                	}
	                	if($("#biztypeValue").val()=="JHJJ_JRZX"){
	                    	$("#biztype1").attr("checked", false);
	                    	$("#biztype1").attr("disabled", true);
	                	}
                	}
                }

            });
            
            $('#cyclesCheckable').change(function() {
                var checked = $('#cyclesCheckable').prop("checked");
                $("input[name='cycles']").prop("disabled", !checked);
                if (!checked) {
                    $("input[name='cycles']").prop("checked", false);
                    getPeople();
                }

            });
            $('#grouptypeCheckable').change(function() {
                var checked = $('#grouptypeCheckable').prop("checked");
                $("input[name='grouptype']").prop("disabled", !checked);
                if (!checked) {
                    $("input[name='grouptype']").prop("checked", false);
                    getPeople();
                }
            });
            $('#statusCheckable').change(function() {
                var checked = $('#statusCheckable').prop("checked");
                $("input[name='status']").prop("disabled", !checked);
                if (!checked) {
                    $("input[name='status']").prop("checked", false);
                    getPeople();
                }
            });
            $('#groupCheckable').change(function() {
                var checked = $('#groupCheckable').prop("checked");
                $('#groupList').prop("disabled", !checked);
                if (!checked) {
                    $('#groupList').val("");
                    $('#s2id_groupList span.select2-chosen').text("");
                    getPeople();
                }
            });
            $('#organization').change(function() {
                var checked = $('#organization').prop("checked");
                $('#organizationName').prop("disabled", !checked);
                if (!checked) {
                    $('#organizationName').val("");
                    $('#s2id_organizationName span.select2-chosen').text("选择");
                    getPeople();
                }
            });
            $("input[name='cycles']").change(getPeople);
            $("input[name='grouptype']").change(getPeople);
            $("input[name='status']").change(getPeople);
            $("#groupList").change(getPeople);
            $("#organizationName").change(getPeople);
            $("#search").click(getPeople);
            $("input[name='biztype']").change(getPeople);

            $('#distributionSave').click(function() {
                confirmx('是否确认分案?', saveConfirm);
			}); 
			
			$('#esc').click(function() {
				window.parent.window.jBox.close();    
			});

			function saveConfirm(){
                if ($("#inputForm").valid()) {
                    if (!$("#rightContainer div").length) {
                        $.jBox.tip("请选择需要分案的催收人员", "warning");
                        return;
                    }
                    
                    $("#distributionSave").attr('disabled',"true");
                    $.ajax({
                        type: 'POST',
                        url : "${ctx}/dunning/tMisDunningOuterTask/outDistributionSave",
                        data: $('#inputForm').serialize(),             //获取表单数据
                        success : function(data) {
                            if (data == "OK") {
                                alert("保存成功");
                                window.parent.page();                         //调用父窗体方法，当关闭子窗体刷新父窗体
                                window.parent.window.jBox.close();            //关闭子窗体
                            } else {
                                alert(data);
                                window.parent.page();
                                window.parent.window.jBox.close();
                            }
                        },
                        error : function(XMLHttpRequest, textStatus, errorThrown){
                            //通常情况下textStatus和errorThrown只有其中一个包含信息
                            alert("保存失败:"+textStatus);
                        }
                    });
                }
			}
		});
	</script>

	<style type="text/css">
		input{border:none;}
		textarea{ resize:none;}
		.people {
			border:solid 1px white;
			padding: 2px;
			cursor: pointer;
		}
		.peoplesContainer {
			width: 35%;
			height: 90px;
			display: inline-block;
			vertical-align: top;
			border: solid 1px #89c8ec;
			overflow-y: auto;
		}
	</style>

</head>
<body>
	<ul class="nav nav-tabs">
	</ul>
	<br/>
	<div class="form-horizontal" style="margin-left: 20px;">
		<div>
			<p style="font-size: medium;color: #1a1a1a">条件快捷筛选</p>
		</div>
		<div id="allpeople">
			<div class="control-group">
				<div class="control-group">
					<input type="hidden" id="biztypeValue" value="${bizType}"/>
					<div style="width:20%;display:inline-block;">
						<input id="biztypes" type="checkbox"/><label >产品</label>
					</div>
					<div style="width:40%;display:inline-block;">
						<input id="biztype0" type="checkbox" name="biztype" value="JHJJ_JRZX" disabled/><label for="cycle0">weixin36<label/>&nbsp;
						<input id="biztype1" type="checkbox" name="biztype" value="JHJJ" disabled/><label for="cycle1">mo9</label>&nbsp;
					</div>
				</div>
				<div style="width:20%;display:inline-block;">
					<input id="cyclesCheckable" type="checkbox"/><label for="cyclesCheckable">队列</label>
				</div>
				<div style="width:50%;display:inline-block;">
					<input id="cycle0" type="checkbox" name="cycles" value="Q0" disabled/><label for="cycle0">Q0<label/>&nbsp;
					<input id="cycle1" type="checkbox" name="cycles" value="Q1" disabled/><label for="cycle1">Q1</label>&nbsp;
					<input id="cycle2" type="checkbox" name="cycles" value="Q2" disabled/><label for="cycle2">Q2<label/>&nbsp;
					<input id="cycle3" type="checkbox" name="cycles" value="Q3" disabled/><label for="cycle3">Q3</label>&nbsp;
					<input id="cycle4" type="checkbox" name="cycles" value="Q4" disabled/><label for="cycle4">Q4</label>&nbsp;
					<input id="cycle5" type="checkbox" name="cycles" value="Q5" disabled/><label for="cycle5">Q5</label>
				</div>
			</div>
			<div class="control-group">
				<div style="width:20%;display:inline-block;">
					<input id="grouptypeCheckable" type="checkbox"/><label for="grouptypeCheckable">催收员类型</label>
				</div>
				<div style="width:40%;display:inline-block;">
					<input id="group1" type="checkbox" name="grouptype" value="selfSupport" disabled/><label for="group1">自营</label>&nbsp;
					<input id="group2" type="checkbox" name="grouptype" value="outsourceSeat" disabled/><label for="group2">外包席坐</label>&nbsp;
					<input id="group3" type="checkbox" name="grouptype" value="outsourceCommission" disabled/><label for="group3">委外佣金</label>
				</div>
			</div>
			<div class="control-group">
				<div style="width:20%;display:inline-block;">
					<input id="statusCheckable" type="checkbox"/><label for="statusCheckable">账号分案状态</label>
				</div>
				<div style="width:40%;display:inline-block;">
					<input id="status1" type="checkbox" name="status" value="t" disabled/><label for="status1">开启</label>&nbsp;
					<input id="status2" type="checkbox" name="status" value="f" disabled/><label for="status2">停止</label>
				</div>
			</div>
		</div>

		<shiro:hasPermission name="dunning:tMisDunningTask:leaderview">
		<div class="control-group" style="height: 40px;">
			<div style="width:20%;display:inline-block;">
				<input id="groupCheckable" type="checkbox"/><label for="groupCheckable">催收小组</label>
			</div>
			<div style="width:40%;display:inline-block;">
				<select id="groupList" disabled class="input-medium">
					<option value="" >选择</option>
					<!-- 添加组类型为optgroup -->
					<c:forEach items="${groupTypes}" var="type">
						<optgroup label="${type.value}">
							<!-- 添加类型对应的小组 -->
							<c:forEach items="${groupList}" var="item">
								<c:if test="${item.type == type.key}">
									<option value="${item.id}" groupType="${item.type}">${item.name}</option>
								</c:if>
							</c:forEach>
						</optgroup>
					</c:forEach>
				</select>
			</div>
		</div>
		</shiro:hasPermission>
		<div class="control-group" style="height: 40px;">
			<div style="width:20%;display:inline-block;">
				<input id="organization" type="checkbox"/><label for="groupCheckable">催收机构</label>
			</div>
			<div style="width:40%;display:inline-block;">
				<select id="organizationName" disabled class="input-medium">
					<option value="" >选择</option>
					<c:forEach items="${organizations}" var="item">
						<option value="${item.id}" >${item.name}</option>
					</c:forEach>
				</select>
			</div>
		</div>

		<div>
			<p style="font-size: medium;color: #1a1a1a">人员筛选</p>
		</div>

		<div class="container" style="width:100%;margin-bottom:8px;">
			<div style="position:relative;width:30%;">
				<input id="searchName" type="text" style="width:183px;" maxlength="10"/>
				<i id="search" class="icon-search" style="position:absolute;top:7px;left:175px;cursor:pointer;"></i>
			</div>
		</div>
	</div>
	<form id="inputForm" class="form-horizontal" style="margin-left: 20px;">
		<div class="container" style="width:100%;height:120px;">
			<div id="template" class="people" peopleId="" style="white-space:nowrap;display:none;" onclick="selectPeople(this);">
				<span id="peopleName"></span>
				<span style="display:none;" onclick="deletePeople(this);">&times;</span>
				<input type="hidden"/>
			</div>
			<div id="leftContainer" class="peoplesContainer">

			</div>
			<div style="width:20%;height:100px;text-align:center;padding:20px 0px 20px 0px;display:inline-block;">
				<input type="button" value="--->" style="border-radius:4px;" onclick="leftMoveToRight();"/><br/>
				<input type="button" value="->>" style="border-radius:4px;margin-top:5px;" onclick="leftAllMoveToRight();"/>
			</div>
			<div id="rightContainer" class="peoplesContainer">

			</div>
		</div>

		<div>
			<p style="font-size: medium;color: #1a1a1a">分案计算</p>
		</div>
		<span>已选案件数:</span><span id="selectedOrders"></span>
		<span style="margin-left:100px;">已选人员数:</span><span id="selectedDunningPeople">0</span>

		<div class="control-group" >
			<div style="width:20%;display:inline-block;">
				<p>委外截止日期：</p>
			</div>
			<div style="width:40%;display:inline-block;">
				<input name="outsourcingenddate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'%y-%M-%d'});"/>
			</div>
		</div>
		
		<input type="hidden" id="orders" name="orders"/>
		<input type="hidden" id="dunningcycle" name="dunningcycle" value="${dunningcycle}"/>
		
		<div class="form-actions">
            <input id="distributionSave" class="btn btn-primary" type="button" value="分案"/>&nbsp;
            <input id="esc" class="btn btn-primary" type="button" value="取消"/>&nbsp;
		</div>
	</form>
	
</body>
</html>

