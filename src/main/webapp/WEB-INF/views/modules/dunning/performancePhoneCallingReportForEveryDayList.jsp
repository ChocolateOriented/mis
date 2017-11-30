<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收员工日报</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            isHide = '0';
		    if ("${isHide}" != '1') {
                $("#advancedSearch").hide();//默认隐藏
                clearSearch();//清空查询条件
            }else {
            	isHide = "${isHide}";
                $("#isHide").val(isHide);
		    }
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出软电话通话详单数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/dunning/report/softPhoneCommunicateReportExportForEveryDayList");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});


            var groups = [];
            if ("${supervisorLimit}" == "true") {
                var groupOptions = $("#groupList")[0].options;
                for (var i = 0; i < groupOptions.length; i++) {
                    if (groupOptions[i].value) {
                        groups.push(groupOptions[i].value);
                    }
                }
            }


            if ("${dunningCommissioner}" == "true"){
                $("#queryfrom").attr("style","display:none;");
			}

			//组与花名联动查询
			$("#groupList").on("change",function(){
			    $("#dunningName").val('');
			    $("#peopleId").val('');
				$("#dunningPeople").select2("val", null);
			});



			$("#dunningName").select2({//
			    ajax: {
			        url: "${ctx}/dunning/tMisDunningPeople/optionList",
			        dataType: 'json',
			        quietMillis: 250,
			        data: function (term, page) {//查询参数 ,term为输入字符
                        var groupId=$("#groupList").val();
                        var param = {};
                        if ("${supervisorLimit}" == "true") {
                            param = {'group.id': groupId,
                                'group.groupIds': groups.toString(),
                                nickname: term};
                        } else {
                            param = {'group.id': groupId,
                                nickname: term};
                        }
                        return param;
			        },
			        results: function (data, page) {//选择要显示的数据
                      var resultsData = [] ;
                      resultsData[0] = {id:null,name:"全部人员",value:""};
                      for (var i = 0; i < data.length; i++) {
                        resultsData[i+1] = {id:data[i].name,name:data[i].name,value:data[i].id};
                      }
                      return { results: resultsData };
			        },
			        cache: true
			    },
			    formatResult:formatPeopleList, //选择显示字段
			    formatSelection:formatPeopleList, //选择选中后填写字段
			    initSelection: function(element, callback) {//回显
                  var name=$(element).val();
                  if (name=="") {
                    return;
                  }
                  callback({id:name,name:name})
			    },
		        width:170
			});

		});
		
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			if (!compareTime()){
                alert("起始时间大于开始时间，或查询时间区间大于一周，请输入正确格式");
                return false;
			}
			$("#searchForm").attr("action","${ctx}/dunning/tMisCallingRecord/getPhoneCallingReportForEveryDay");
			$("#searchForm").submit();
        	return false;
        }

		//格式化peopleList选项
		function formatPeopleList( item ){
			var name = item.name ;
			if(name == null || name ==''){
				name = "空" ;
            }
			if ((item.value != null || item.value !='') && item.name !="全部人员" && item.name !=""){
				$("#peopleId").val(item.value);
			}
			if (item.name =="全部人员"){
                $("#peopleId").val('');
			}
			return name ;
		}


		function showAndHideSearch(){
            if (isHide == '0'){
                $("#advancedSearch").show();
                isHide = '1'
                $("#isHide").val(isHide);
            }else {
                $("#advancedSearch").hide();
                isHide = '0';
                $("#isHide").val(isHide);
            }
		}

		function clearSearch(){
		    $("#department").val("");
		    $("#queue").val("");
		    $("#onTimeStart").val("");
		    $("#onTimeEnd").val("");
		    $("#breaktimeStart").val("");
		    $("#breaktimeEnd").val("");
		    $("#extension").val("");
		    $("#callDurationStart").val("");
		    $("#callDurationEnd").val("");
		}

        function compareTime(){
            var startTime=$("#datetimestart").val();
            var start=new Date(startTime.replace("-", "/").replace("-", "/"));
            var endTime=$("#datetimeend").val();
            var end=new Date(endTime.replace("-", "/").replace("-", "/"));
            if(end<start){
                return false;
            }
            if (end - start > 604740000){
                return false;
            }
            return true;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/dunning/tMisCallingRecord/getPhoneCallingReportForEveryDay">软电话通话详单(每日)</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="dunningPhoneReportFile" action="${ctx}/dunning/tMisCallingRecord/getPhoneCallingReportForEveryDay" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="isHide"  name="isHide" type="hidden" />
		<ul class="ul-form">
		
			<li><label>时间：</label>
				<input id="datetimestart" name="datetimestart" path="datetimestart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningPhoneReportFile.datetimestart}" pattern="yyyy-MM-dd HH:mm"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});"/>-
				<input id="datetimeend" name="datetimeend"  path="datetimeend" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${dunningPhoneReportFile.datetimeend}" pattern="yyyy-MM-dd HH:mm"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',isShowClear:false});"/>
			</li>
			<div id="queryfrom">
				<li>
					<label>催收小组：</label>
						<form:select id="groupList" path="groupId" class="input-medium">
							<form:option value="">全部</form:option>
							<!-- 添加组类型为optgroup -->
							<c:forEach items="${groupTypes}" var="type">
								<optgroup label="${type.value}">
									<!-- 添加类型对应的小组 -->
									<c:forEach items="${groupList}" var="item">
										<c:if test="${item.type == type.key}">
											<option value="${item.id}" <c:if test="${dunningPhoneReportFile.groupId == item.id }">selected="selected"</c:if>>${item.name}</option>
										</c:if>
									</c:forEach>
								</optgroup>
							</c:forEach>
						</form:select>
				</li>
			<li>
				<label>催收人：</label>
				<form:input id="dunningName" name="dunningName" path="peopleName" type="hidden" value=""/>
				<form:input id="peopleId" name="peopleId" path="peopleId" type="hidden" value="${dunningPhoneReportFile.peopleId}"></form:input>
			</li>
			</div>
			<div align="right">
			<li class="btns">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
			<input id="btnExport" class="btn btn-primary" type="button" value="导出" /></li>
			&nbsp;
				<a href="javascript:void(0)" style="height:40px;line-height:40px;" onclick="showAndHideSearch()">>高级搜索</a>
			<li class="clearfix"></li>
			</div>

			<div id="advancedSearch">
				<li>
					<label>机构：</label>
					<form:input id="department" path="department"/>
				</li>
				<li>
					<label>队列：</label>
					<form:input id="queue" path="queue"/>
				</li>
				<li>
					<label>在线时长</label>
					<form:input path="onTimeStart" id="onTimeStart"   style="width:90px;" /> - <form:input path="onTimeEnd" id="onTimeEnd" style="width:90px;" />
				</li>
				<li class="clearfix"></li>
				<li>
					<label>小休时长</label>
					<form:input path="breaktimeStart" id="breaktimeStart"  style="width:90px;" /> - <form:input path="breaktimeEnd" id="breaktimeEnd"  style="width:90px;" />
				</li>
				<li>
					<label>分机号：</label>
					<form:input id="extension" path="extension"/>
				</li>
				<li>
					<label>通话时长</label>
					<form:input path="callDurationStart" id="callDurationStart"  style="width:90px;" /> - <form:input path="callDurationEnd" id="callDurationEnd"  style="width:90px;" />
				</li>
			</div>
			
		</ul>
	</form:form>
	<sys:message content="${message}"/>
		<table id="contentTable"  class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>日期</th>
					<th>小组</th>
					<th>姓名</th>
					<th>坐席ID</th>
					<th>分机号</th>
					<th>机构</th>
					<th>队列</th>
					<th>签入时间</th>
					<th>签出时间</th>
					<th>在线时长(秒)</th>
					<th>小休时长（秒）</th>
					<th>拨打电话量</th>
					<th>处理案件量</th>
					<th>接通量</th>
					<th>接通率</th>
					<th>通话时长（秒）</th>
					<th>平均每小时拨打量</th>
					<th>平均每小时接通量</th>
					<th>平均每小时通话时长（秒/小时）</th>
					<th>平均每小时处理量</th>
				</tr>

			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="callingReport">
				<tr>
					<td  style="word-break: keep-all;white-space:nowrap;">
						${callingReport.dateTime}
					</td>
					<td  style="word-break: keep-all;white-space:nowrap;">
						${callingReport.groupName}
					</td>
					<td style="word-break: keep-all;white-space:nowrap;">
						${callingReport.dunningName}
					</td>
					<td style="word-break: keep-all;white-space:nowrap;">
						${callingReport.agent}
					</td>
					<td style="word-break: keep-all;white-space:nowrap;">
						${callingReport.extension}
					</td>
					<td style="word-break: keep-all;white-space:nowrap;">
						${callingReport.department}
					</td>
					<td>
						${callingReport.queue}
					</td>
					<td style="word-break: keep-all;white-space:nowrap;">
						${callingReport.firstCheckIn}
					</td>
					<td style="word-break: keep-all;white-space:nowrap;">
						${callingReport.lastCheckOut}
					</td>
					<td>
						${callingReport.ontime}
					</td>
					<td>
						${callingReport.breaktime}
					</td>
					<td>
						${callingReport.callingAmount}
					</td>
					<td>
						${callingReport.dealCaseAmount}
					</td>
					<td>
						${callingReport.connectAmout}
					</td>
					<td>
						${callingReport.connectRate}
					</td>
					<td>
						${callingReport.callDuration}
					</td>
					<td>
						${callingReport.callingAmountOnHour}
					</td>
					<td>
						${callingReport.connectAmountOnHour}
					</td>
					<td>
						${callingReport.callDurationOnHour}
					</td>
					<td>
						${callingReport.dealCaseAmountOnHour}
					</td>
				</tr>
			</c:forEach>


			</tbody>
		</table>
		<div class="pagination">${page}</div>
</body>
</html>