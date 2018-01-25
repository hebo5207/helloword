<%--
  Created by IntelliJ IDEA.
  User: heyon
  Date: 2018/1/22
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="static/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="static/js/mustache-2.3.0.js"></script>

<%-- 导航菜单提醒 --%>
<script id="navTemplate" type="x-tmpl-mustache">
     <span class="c-gray en">&gt;</span> {{str}}
</script>

<script type="application/javascript">
    function addNav(id) {
        $.ajax({
            url:"/sys/menu/navStr?id=" + id,
            success:function (data) {
                if(data.ret){
                    var navTemplate = $("#navTemplate").html();
                    Mustache.parse(navTemplate);
                    $(data.data).each(function(i,datas){
                        var reder = Mustache.render(navTemplate,{str:datas});
                        $("#addNav").before(reder);
                    });
                }else{
                    alert("获取导航失败，" + data.data)
                }
            }
        })
    }
</script>