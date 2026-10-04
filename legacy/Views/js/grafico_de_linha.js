 var ctx = document.getElementById("myChart").getContext('2d');
 var bm1 = document.getElementById("bm1").value;
 var bm2 = document.getElementById("bm2").value;
 var bm3 = document.getElementById("bm3").value;
 var bm4 = document.getElementById("bm4").value;
 

		var myChart = new Chart(ctx, {
		    type: 'line',
		    data: {
		        labels: ["1° Bimnestre", "2° Bimnestre", "3° Bimnestre", "4° Bimnestre"],
		        datasets: [{
		            label: 'MEDIA DE NOTAS',
		            data: [bm1, bm2, bm3, bm4],
		            backgroundColor: [
		                'rgba(255, 99, 132, 0.6)',
		                'rgba(54, 162, 235, 0.6)',
		                'rgba(255, 206, 86, 0.2)',
		                'rgba(75, 192, 192, 0.2)',
		                'rgba(153, 102, 255, 0.2)',
		                'rgba(255, 159, 64, 0.2)'
		            ],
		            borderColor: [
		                'rgba(255,99,132,1)',
		                'rgba(54, 162, 235, 1)',
		                'rgba(255, 206, 86, 1)',
		                'rgba(75, 192, 192, 1)',
		                'rgba(153, 102, 255, 1)',
		                'rgba(255, 159, 64, 1)'
		            ],
		            borderWidth: 1,
		            fill:false
		        }]


		    },
		    options: {
		        scales: {
		            yAxes: [{
		                ticks: {
		                    beginAtZero:true

		                }
		            }]
		        }
		    }
		});