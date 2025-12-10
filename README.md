# study-platform
Collaborative Study Platform
Aplikácia je určená pre študentov, ktorí chcú efektívne spolupracovať pri štúdiu. Umožňuje:
vytvárať a spravovať študijné skupiny,
pridávať a sledovať úlohy,
zdieľať materiály,
komunikovať v reálnom čase cez notifikácie.

Cieľ: zjednodušiť tímovú prácu, zvýšiť prehľad o aktivitách a podporiť organizáciu štúdia.

Architektúra systému
Architektúra sa riadi modelom klient-server:
Frontend (JavaFX)
poskytuje používateľské rozhranie,
umožňuje registráciu, prihlásenie, správu skupín a úloh.

Backend (Spring Boot)
spracováva požiadavky klienta,
poskytuje REST API a WebSocket endpointy,
obsahuje obchodnú logiku a bezpečnostné mechanizmy.

Databáza (SQLite)
ukladá používateľov, skupiny, úlohy, materiály a logy aktivít.

Databázový model (ER diagram)
<img width="1017" height="857" alt="image" src="https://github.com/user-attachments/assets/cd86c95a-43d5-4e6c-b6fb-d718430aa63d" />

Tabuľky:

User – údaje o používateľoch (meno, email, heslo).
Group – študijné skupiny.
Membership – členstvo používateľov v skupinách.
Task – úlohy priradené ku skupinám.
Resource – zdieľané materiály.
ActivityLog – záznamy o aktivitách.

Väzby:
Používateľ ↔ Skupina (cez Membership).
Skupina ↔ Úlohy, Materiály.
Používateľ ↔ ActivityLog (sledovanie akcií).


REST API
POST /users/register – registrácia používateľa
POST /users/login – prihlásenie
GET /groups – zoznam skupín
POST /groups – vytvorenie skupiny
PUT /groups/{id} – úprava skupiny
DELETE /groups/{id} – zmazanie skupiny
POST /tasks – pridanie úlohy
PUT /tasks/{id} – zmena stavu úlohy
DELETE /tasks/{id} – zmazanie úlohy

WebSocket
/ws/notifications – odosielanie notifikácií o nových úlohách, materiáloch alebo členoch skupiny.

