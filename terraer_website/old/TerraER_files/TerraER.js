// Created by iWeb 3.0.1 local-build-20100507

setTransparentGifURL('Media/transparent.gif');function applyEffects()
{var registry=IWCreateEffectRegistry();registry.registerEffects({stroke_0:new IWPhotoFrame([IWCreateImage('TerraER_files/Freestyle_01.png'),IWCreateImage('TerraER_files/Freestyle_02.png'),IWCreateImage('TerraER_files/Freestyle_03.png'),IWCreateImage('TerraER_files/Freestyle_06.png'),IWCreateImage('TerraER_files/Freestyle_09.png'),IWCreateImage('TerraER_files/Freestyle_08.png'),IWCreateImage('TerraER_files/Freestyle_07.png'),IWCreateImage('TerraER_files/Freestyle_04.png')],null,2,0.800000,3.000000,3.000000,3.000000,3.000000,22.000000,24.000000,23.000000,25.000000,166.000000,222.000000,166.000000,222.000000,null,null,null,0.100000),shadow_0:new IWShadow({blurRadius:3,offset:new IWPoint(-1.4142,1.4142),color:'#463c3c',opacity:0.750000})});registry.applyEffects();}
function hostedOnDM()
{return false;}
function onPageLoad()
{loadMozillaCSS('TerraER_files/TerraERMoz.css')
adjustLineHeightIfTooBig('id1');adjustFontSizeIfTooBig('id1');detectBrowser();Widget.onload();fixupAllIEPNGBGs();fixAllIEPNGs('Media/transparent.gif');fixupIECSS3Opacity('id2');applyEffects()}
function onPageUnload()
{Widget.onunload();}
