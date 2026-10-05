(function($) {
	
	/*------------------
		Brands Slider
	--------------------*/
	$('.product-slider').owlCarousel({
		loop: false,
		nav: true,
		dots: true,
		margin : 20,
		autoplay: false,
		navText: ['<i class="flaticon-left-arrow-1"></i>', '<i class="flaticon-right-arrow-1"></i>'],
		responsive : {
			0 : {
				items: 1,
			},
			480 : {
				items: 1,
			},
			768 : {
				items: 2,
			},
			1200 : {
				items: 4,
			}

		}
	});

})(jQuery);
