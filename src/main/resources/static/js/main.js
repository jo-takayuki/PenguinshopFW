//郵便番号の検索
$('#search').click(function() {
	$.ajax({
		url: 'https://zipcloud.ibsnet.co.jp/api/search?zipcode='
			+ $('#zipcode').val(),
		type: 'GET',
		dataType: 'JSONP'
	}).done(function(res) {
		console.log(res);
		if (res.status != 200) {
			alert(res.message);
		} else {
			const r = res.results[0];
			const address = r.address1
				+ r.address2
				+ r.address3;
			$('#address').val(address);
		}
	}).fail(function() {
		alert('通信に失敗しました');
	});
	return false;
});

//フォームの送信時の確認
$('#form01').submit(function() {
	const result = confirm('登録しますか？');
	if (!result) {
		return false;
	}
});

//登録画像の編集
let key = 0;
function loadImage(obj) {
	for (i = 0; i < obj.files.length; i++) {
		var fileReader = new FileReader();
		fileReader.onload = (function(e) {
			var field = document.getElementById("imgPreviewField");
			var figure = document.createElement("figure");
			var rmBtn = document.createElement("input");
			var img = new Image();
			img.src = e.target.result;
			rmBtn.type = "button";
			rmBtn.name = key;
			rmBtn.value = "削除";
			rmBtn.onclick = (function() {
				var element = document.getElementById(
					"img-" + String(rmBtn.name)).remove();
			});
			figure.setAttribute("id", "img-" + key);
			figure.appendChild(img);
			figure.appendChild(rmBtn)
			field.appendChild(figure);
			key++;
		});
		fileReader.readAsDataURL(obj.files[i]);
	}
}

//item削除の確認
$('#form02').submit(function() {
	const result = confirm('登録しますか？');
	if (!result) {
		return false;
	}
});
$('.itemDelete').click(function() {
	const result = confirm('削除しますか？');
	if (!result) {
		return false;
	}
});

//スライドショー
$('#slider').slick({
	autoplay: true,         //自動再生
	autoplaySpeed: 2000,    //自動再生のスピード
	speed: 800,	            //スライドするスピード
	dots: true,             //スライドしたのドット
	arrows: true,           //左右の矢印
	infinite: true,         //スライドのループ
});
