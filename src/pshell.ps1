$step=$args[0]
$objectExcel = new-object -c excel.application
$objectExcel.displayAlerts = $false # don't prompt the user
$objectExcel.visible = $False;
$Classeur = $objectExcel.workbooks.open($step, $null)
$objectExcel.run("refreshdata")
$Classeur.saveAs($step)
$Classeur.close($false)
$objectExcel.displayAlerts = $true
$objectExcel.visible = $False;
$objectExcel.quit()
