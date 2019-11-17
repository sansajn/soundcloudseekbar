ako na wave bar ?

na podfarbovanie časti wave baru môžem použiť napr. blend operáciu multiply, kde wave bar bude biely (hodnota 1.0 pre každú zložku) na priesvitnom pozadí kde cez neho nakreslím červený obdĺžnik s multiply blend modom čo zafarbí biele časti na červeno (pozri GIMP ukážku `wave_bar.xcf`).

pomocou ADD operácie môžem realizovať požadovanú opéráciu za predpokladu, že priebeh signálu bude reprezentovaný čiernou farbou na priehľadnom pozadí a masku vytvorím ako bitmapu 1xN (na zväčšenie môžem použiť transformáciu) v ktorej sú časti signálu (minulosť, skok a budúcnosť) reprezentované príslušnou farbou.


(?) Ako fungujú blend oprácie v Androide ?

https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/graphics/Xfermodes.java
