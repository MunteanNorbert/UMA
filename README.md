# UMA – Aplicație mobilă pentru gestionarea studenților și angajaților unei universități

## Cuprins

* Descrierea proiectului
* Technologii utilizate
* Rularea Aplicației

## Descrierea proiectului

Aplicația pentru mobile UMA, are rolul de a gestiona studenți și angajați unei universități, oferindu-le acestora posibilitatea de a își gestiona activitățile zilnice cu ușurință și facilitând comunicarea între utilizatori.

Aplicația a fost dezvoltată în mediul de dezvoltare Android Studio, utilizând Java ca limbaj de programare și Google Firebase ca bază de date.

Pentru rularea aplicației, este indicată utilizarea mediului de dezvoltare Android Studio care poate fi descărcat gratuit de pe pagina oficilă Android Studio: https://developer.android.com/studio .

## Technologii utilizate

* [Java](https://www.oracle.com/java/technologies/javase-downloads.html)
* [Android Studio (Mediul de dezvoltare)](https://developer.android.com/studio)
* [Google Firebase (Baza de date)](https://www.dizitart.org/nitrite-database.html) 

## Rularea Aplicației

Pentru a putea testa aplicația dezvoltată, aceasta poate fi rulată pe emulatorul conținut în cadrul mediului de dezvoltare Android Studio.
Pentru a rula aplicația, se va da click pe meniul "Run" și se va selecta "Run 'app'".
Ca și alternativă se poate utiliza și comanda "gradlew installDebug" în terminalul mediului de dezvoltare Android Studio. 
Aceasta va da build fișierului de tip APK și îl va instala pe emulator, astfel putând fi utilizată aplicația.

În același timp, pentru a putea testa aplicația dezvoltată pe un dispoziti fizic (de exemplu un telefon inteligent), se poate crea un fișier de tip APK.
Pentru a crea fișierul de tip APK, se va da click pe meniul "Build" și se va selecta "Build Bundle(s)/APK(s)" iar mai apoi se va selecta "Build APK(s)".
Fișierul de tip APK creat, poate fi găsit în folderul "'Numele_Proiectului'\app\build\outputs\apk\debug" (de exemplu "UMA\app\build\outputs\apk\debug").
Fișierul va avea numele "app-debug.apk". 
Ca și alternativă se poate utiliza și comanda "gradlew assembleDebug" în terminalul mediului de dezvoltare Android Studio. 
Aceasta va crea la rândul ei fișirul de tip APK, acesta putând fi găsit în același folder.