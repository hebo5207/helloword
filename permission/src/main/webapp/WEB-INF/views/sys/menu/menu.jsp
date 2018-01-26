<%--
  Created by IntelliJ IDEA.
  User: heyon
  Date: 2018/1/23
  Time: 16:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
    <base href="<%=basePath%>"/>
    <jsp:include page="../../common/top.jsp"/>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <!--[if lt IE 9]>
    <script type="text/javascript" src="lib/html5shiv.js"></script>
    <script type="text/javascript" src="lib/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" type="text/css" href="lib/Hui-iconfont/1.0.8/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="static/h-ui.admin/skin/default/skin.css" id="skin"/>
    <link rel="stylesheet" type="text/css" href="static/h-ui.admin/css/style.css"/>
    <!--[if IE 6]>
    <script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js"></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <title>菜单信息</title>

</head>
<body class="pos-r">
<input class="getPageId" name="<%=request.getAttribute("id")%>" style="display: none">
<div class="pos-a"
     style="width:200px;left:0;top:0; bottom:0; height:100%; border-right:1px solid #e5e5e5; background-color:#f5f5f5; overflow:auto;">
    <ul id="treeDemo" class="ztree"></ul>
</div>

<%--默认是展示这个菜单的信息--%>
<div style="margin-left:200px;">
    <nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <a id="addNav" class="btn btn-success radius r"
                                                                       style="line-height:1.6em;margin-top:3px"
                                                                       href="javascript:location.replace(location.href);"
                                                                       title="刷新"><i
            class="Hui-iconfont">&#xe68f;</i></a></nav>
    <iframe ID="testIframe" Name="testIframe" FRAMEBORDER=0 SCROLLING=AUTO src="sys/menu/toMenuAddPage.page?type=info&id=-1"
            width=100% height=500></iframe>
</div>


<!--请在下方写此页面业务相关的脚本-->
<link rel="stylesheet" href="lib/zTree/v3/css/metroStyle/metroStyle.css" type="text/css">
<script type="text/javascript" src="lib/zTree/v3/js/jquery.ztree.all-3.5.min.js"></script>

<SCRIPT type="text/javascript">
    var setting = {
        view: {
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,

        },
        edit: {
            enable: true,
            drag: {
                autoExpandTrigger: false,
                isCopy: false,
                isMove: false
            }
        },
        callback: {
            beforeEditName: zTreeBeforeEditName,
            beforeRemove: zTreeBeforeRemove,
            onClick: zTreeOnClick
        }
    };

    /*修改按钮*/
    function zTreeBeforeEditName(treeId, treeNode) {
        initIframe("update", treeNode.id);
        return false;
    }

    // 删除按钮
    function zTreeBeforeRemove(treeId, treeNode) {
      $.ajax({
          url:"sys/menu/delete.json?id=" + treeNode.id,
          success:function(result) {
            if(result.ret){
                layer.msg("删除成功", {icon: 1});
                setTimeout(function () {
                    window.location.reload();
                },1000);
            }else{
                layer.msg("删除失败，" + result.msg, {icon: 5});
            }
          }
      });
        return false;
    }

    /*添加按钮*/
    function zTreeBeforeAdd(menuId) {
        initIframe("add",menuId);
    }

    /*节点点击的时候*/
    function zTreeOnClick(event, treeId, treeNode) {
        initIframe("info", treeNode.id);
    };


    // 界面初始化
    $(document).ready(function () {
        var zNodes;
        $.ajax({
            async: false,
            url: "sys/index/initMenuTree",
            success: function (ret) {
                if (ret.ret) {
                    zNodes = ret.data;
                    $(zNodes).each(function (i, znode) {
                        znode.icon = "";
                        znode.url = "";
                    })
                } else {
                    alert("加载失败");
                }
            }
        });
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);

    });


    /*鼠标经过节点的方法  增加一个+号*/
    function addHoverDom(treeId, treeNode) {
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
        /* 如果是二级菜单就不显示添加按钮 */
        if (treeNode.parentTId != undefined) return;
        var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
            + "' title='add node' onfocus='this.blur();'></span>";
        sObj.after(addStr);
        var btn = $("#addBtn_" + treeNode.tId);
        /*点击+号的方法*/
        if (btn) btn.bind("click", function () {
            zTreeBeforeAdd(treeNode.id);
            return false;
        });
    };

    /*鼠标离开节点的方法  移除+号*/
    function removeHoverDom(treeId, treeNode) {
        $("#addBtn_" + treeNode.tId).unbind().remove();
    };


    /*初始化IFRAME*/
    function initIframe(type, id) {
        if ($("#testIframe") != null) {
            $("#testIframe").remove();
        }
        var html = " <iframe id=\"testIframe\" Name=\"testIframe\" FRAMEBORDER=0 SCROLLING=AUTO" +
            " width=100%  height=500 SRC=\"sys/menu/toMenuAddPage.page?type=" + type + "&id=" + id + "\"></iframe>";
        $(".breadcrumb").after(html);
    }

</SCRIPT>

<%--初始化导航菜单--%>
<script type="application/javascript">
    var id = $(".getPageId").attr("name");
    addNav(id);
</script>
</body>
</html>
