$(function() {
	/*左侧菜单左右折叠 start*/
    var direction = "left";
    var executed = false;
    var callback = function() {
        if (executed) {
            return;
        }
        executed = true;
        if (direction == "left") {
            direction = "right";
            $(".expand").addClass("right");
        } else {
            direction = "left";
            $(".expand").removeClass("right");
        }
        console.log(direction)
    }
    var running = false;
    var current = 0;
    var heightList = [];
    var subMenuList = $(".sub-menu");

    for (var i = 0, count = subMenuList.length; i < count; i++) {
        var perH = subMenuList.eq(i).height();
        heightList.push(perH);
    }
    subMenuList.not($(".sub-menu li.active").parent()).height(0);
    var timeSpan = 500;
    $(".expand").click(function() {
        executed = false;
        if (direction == "left") {
            current = heightList[subMenuList.index($(".menu li.active").parent())];
            console.log(current);
            $(".sub-menu").stop().animate({
                height: 0
            }, timeSpan);
            $(".sidebars").stop().animate({
                width: 42
            }, timeSpan);
            $(".menu a").stop().animate({
                paddingLeft: 5
            }, timeSpan, callback);
        } else {
            console.log($(".menu li.active").parent());
            $(".menu li.active").parent().stop().animate({
                height: current
            }, timeSpan);
            $(".sidebars").stop().animate({
                width: 207
            }, timeSpan);
            $(".menu a").stop().animate({
                paddingLeft: 28
            }, timeSpan, callback);
        }

    });
/*左侧菜单左右折叠 end*/

/*左侧菜单上下折叠 start*/
    $(".menu li >a").on("click", function() {

        var index = subMenuList.index($(this).next('.sub-menu'));
        $(this).next('.sub-menu').addClass("active").stop().animate({
            height: heightList[index]
        }, 300);
        $(".sub-menu").not($(this).next('.sub-menu')).removeClass("active").stop().animate({
            height: 0
        }, 300);


    })
    /*左侧菜单折叠 end*/

    /*左侧菜单点击 start*/
    $(".sub-menu li").on("click",function(){
    	var href=$(this).attr("href");
    	$(".content").load(href); 
    	$(".sub-menu li.active").removeClass("active");
    	$(this).addClass("active");
    })
    /*左侧菜单点击 end*/
})
