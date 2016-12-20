function bwcumulativeOrders(url,weekNumber,text){
//		 	var weekNumber = $("#weekNumber").val();
		 	$.post(url, { weekNumber : weekNumber }, function(data) {
	    	var myChart1 = echarts.init(document.getElementById('bwcumulativeOrders_div'));
	       	myChart1.setOption({
	    	title : {
		        text: text,
		        subtext: '最近'+ weekNumber + '周',
	    	},
		    tooltip : {
		        trigger: 'axis'
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            magicType: {show: true, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		     grid:{
					 x:120,
				     x2:40,
			    },
		    xAxis : [
		        {
		            type : 'value',
		            boundaryGap : [0, 0.01]
		        }
		    ],
		    yAxis : [
		        {
		            type : 'category',
		            data : data['intervalDatetime']
		        }
		    ],
		    series : [
		        {
		        	itemStyle:{  
		               normal:{  
		                      label:{show:true}  
		                      ,areaStyle:{color:'green'}   //设置地图背景色的颜色设置  
		                      ,color:'rgb(230, 93, 70)' //刚才说的图例颜色设置  
		               },  
		               emphasis:{label:{show:true}}  
		           	},
		            name:'订单数',
		            type:'bar',
		            data:data['val']
		        }
			]
		    });
		});	
	}