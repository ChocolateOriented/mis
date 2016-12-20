//-------------------------------------加载---------------------------------------------------
function initTree(url,info) {
	$("#tree").tree(
			{
				url : url,
				load : info,
				method : 'POST',
				animate : true,
				checkbox : true,
				//		onlyLeafCheck:true,
				//		dnd : true,
				cascadeCheck : true,//层叠选中  
				lines : true,// 虚线  
				onBeforeLoad : function(node, param) { 
					param.info = info
				},
				onBeforeExpand : function(node, param) { },
				onLoadSuccess : function(node, data) {
					//			var nodeDep = $('#tree').tree('find', checkeid);
				//			if (null != nodeDep && undefined != nodeDep) {
				//				$('#tree').tree('check', nodeDep.target);
				//			}
				},
				onCheck : function(node, checked) {
					if (checked) {
//						alert("选中"+node.id)
					} else {
		
					}
				},
				onClick : function(node, checked) {
//					alert(node.attributes);
				}
			});
}

//-------------------------------------自定小方法---------------------------------------------

// 获取全部选中节点
function getChecked() {
	var nodes = $('#tree').tree('getChecked');
	var s = '';
	for ( var i = 0; i < nodes.length; i++) {
		if (s != '')
			s += ',';
		s += nodes[i].text;
	}
	alert(s);
}

//  重置
function reload() {
	var node = $('#tree').tree('getSelected');
	if (node) {
		$('#tree').tree('reload', node.target);
	} else {
		$('#tree').tree('reload');
	}
}

//  全部子节点
function getChildren() {
	var node = $('#tree').tree('getSelected');
	if (node) {
		var children = $('#tree').tree('getChildren', node.target);
	} else {
		var children = $('#tree').tree('getChildren');
	}
	var s = '';
	for ( var i = 0; i < children.length; i++) {
		s += children[i].text + ',';
	}
	alert(s);
}

// 点击中的节点
function getSelected() {
	var node = $('#tree').tree('getSelected');
	alert(node.text);
}

// 点击中的节点折叠
function collapse() {
	var node = $('#tree').tree('getSelected');
	$('#tree').tree('collapse', node.target);
}
// 点击中的节点开放节点
function expand() {
	var node = $('#tree').tree('getSelected');
	$('#tree').tree('expand', node.target);
}
// 点击中的节点折叠所有的节点们
function collapseAll() {
	var node = $('#tree').tree('getSelected');
	if (node) {
		$('#tree').tree('collapseAll', node.target);
	} else {
		$('#tree').tree('collapseAll');
	}
}
// 点击中的节点开放所有的节点
function expandAll() {
	var node = $('#tree').tree('getSelected');
	if (node) {
		$('#tree').tree('expandAll', node.target);
	} else {
		$('#tree').tree('expandAll');
	}
}

// 追加一些子节点们到一个父节点
function append() {
	var node = $('#tree').tree('getSelected');
	$('#tree').tree('append', {
		parent : (node ? node.target : null),
		data : [ {
			text : 'new1',
			checked : true
		}, {
			text : 'new2',
			state : 'closed',
			children : [ {
				text : 'subnew1'
			}, {
				text : 'subnew2'
			} ]
		} ]
	});
}

// 移除
function remove() {
	var node = $('#tree').tree('getSelected');
	$('#tree').tree('remove', node.target);
}

// 更新
function update() {
	var node = $('#tree').tree('getSelected');
	if (node) {
		node.text = '<span style="font-weight:bold">new text<\/span>';
		node.iconCls = 'icon-save';
		$('#tree').tree('update', node);
	}
}

// 点击中的节点是否有是子节点
function isLeaf() {
	var node = $('#tree').tree('getSelected');
	var b = $('#tree').tree('isLeaf', node.target);
//	alert(b)
}

// 获取子节点或者获取父节点
function getNode(type) {
	var node = $('#tree').tree('getChecked');
	var chilenodes = '';
	var parantsnodes = '';
	var prevNode = '';
	for ( var i = 0; i < node.length; i++) {
		if ($('#tree').tree('isLeaf', node[i].target)) {
			chilenodes += node[i].text + ',';
			var pnode = $('#tree').tree('getParent', node[i].target);
			//			alert(pnode.text);
			if (prevNode != pnode.text) {
				parantsnodes += pnode.text + ',';
				prevNode = pnode.text;
			}

		}
	}
	chilenodes = chilenodes.substring(0, chilenodes.length - 1);
	parantsnodes = parantsnodes.substring(0, parantsnodes.length - 1);
	if (type == 'child') {
		return chilenodes;
	} else {
		return parantsnodes
	}
};

function doNode() {
	var c = "";
	var p = "";
	$(".tree-checkbox1").parent().children('.tree-title').each(function() {
		c += $(this).parent().attr('node-id') + ",";
	});
	$(".tree-checkbox2").parent().children('.tree-title').each(function() {
		p += $(this).parent().attr('node-id') + ",";
	});
	var str = (c + p);
	str = str.substring(0, str.length - 1);
	alert(str);
}

//  验证
function getChildrenTest() {
	var node = $('#tree').tree('getChecked');
	if (node) {
		alert("有子节点");
	} else {
		alert("无子节点");
	}
}

//  var nodes = $('#tree').tree('getChecked','indeterminate');  获取实心圆
//  var nodeDep = $('#tree').tree('find', 1);

