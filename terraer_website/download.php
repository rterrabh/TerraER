<?php
 $Down=$_GET['Down'];
?>

<html>
 <head>
		<script type="text/javascript">
        var _gaq = _gaq || [];
      _gaq.push(['_setAccount', 'UA-8725326-4']);
      _gaq.push(['_trackPageview']);
    
      (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
      })();
    
    </script>
  <meta http-equiv="refresh" content="1;url=<?php echo $Down; ?>">
 </head>
 <body>

 <?php

  $filePath = 'count.txt';

  // If file exists, read current count from it, otherwise, initialize it to 0
  $count = file_exists($filePath) ? file_get_contents($filePath) : 0;

  // Increment the count and overwrite the file, writing the new value
  file_put_contents($filePath, ++$count);

  // Display current download count
  echo "Downloads: " . $count;
 ?> 

 </body>
</html>