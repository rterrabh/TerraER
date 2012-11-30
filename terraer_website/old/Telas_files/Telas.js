// Created by iWeb 3.0.1 local-build-20100507

setTransparentGifURL('Media/transparent.gif');function applyEffects()
{var registry=IWCreateEffectRegistry();registry.registerEffects({shadow_1:new IWShadow({blurRadius:3,offset:new IWPoint(-1.4142,1.4142),color:'#463c3c',opacity:0.750000}),stroke_0:new IWStrokeParts([{rect:new IWRect(-3,4,5,127),url:'Telas_files/stroke.png'},{rect:new IWRect(-3,-2,5,6),url:'Telas_files/stroke_1.png'},{rect:new IWRect(2,-3,223,7),url:'Telas_files/stroke_2.png'},{rect:new IWRect(225,-2,5,6),url:'Telas_files/stroke_3.png'},{rect:new IWRect(225,4,6,127),url:'Telas_files/stroke_4.png'},{rect:new IWRect(225,131,6,5),url:'Telas_files/stroke_5.png'},{rect:new IWRect(2,131,223,6),url:'Telas_files/stroke_6.png'},{rect:new IWRect(-3,131,5,6),url:'Telas_files/stroke_7.png'}],new IWSize(228,135)),shadow_0:new IWShadow({blurRadius:3,offset:new IWPoint(-1.4142,1.4142),color:'#463c3c',opacity:0.750000}),shadow_2:new IWShadow({blurRadius:3,offset:new IWPoint(-1.4142,1.4142),color:'#463c3c',opacity:0.750000})});registry.applyEffects();}
function hostedOnDM()
{return false;}
function onPageLoad()
{loadMozillaCSS('Telas_files/TelasMoz.css')
adjustLineHeightIfTooBig('id1');adjustFontSizeIfTooBig('id1');detectBrowser();Widget.onload();fixupAllIEPNGBGs();fixAllIEPNGs('Media/transparent.gif');fixupIECSS3Opacity('id2');applyEffects()}
function onPageUnload()
{Widget.onunload();}
