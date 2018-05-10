# Currency Service REST alkalmazás

## Telepítési leírás

A telepítés egyszerűsítése miatt a telepítési leírás feltételezi a docker használatát. Amennyiben már van egy működő adatbázis, akkor elég abban lefuttatni a `db/init-currency-db.sql` fájlt a séma létrehozásához.
Az adatbázis verziója 11.2.0.2 XE, mivel én nem találtam sem 12c XE verziót, sem a legújabb 18c XE verzióhoz hivatalos docker image-t.
*  [OJDBC6 driver letöltése](http://www.oracle.com/technetwork/apps-tech/jdbc-112010-090769.html)
* OJDBC6 driver feltöltése a lokális maven repo-ba: `mvn install:install-file -Dfile=ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.2 -Dpackaging=jar`
* [Oracle 11c XE letöltése](http://www.oracle.com/technetwork/database/database-technologies/express-edition/downloads/index.html)
* A letöltött `oracle-xe-11.2.0-1.0.x86_64.rpm.zip` fájl bemásolása az alkalmazás forráskódja mellett található `db/11.2.0.2` mappába
* A db mappában az alábbi parancsot kell kiadni: `./buildDockerImage.sh -v 11.2.0.2 -x`
Ez elkészíti az adatbázishoz tartozó docker image-t, pár percig is eltarthat a futás
* Docker network létrehozása
`docker network create --driver bridge currency_network`
* Adatbázis futtatása
`docker run -d --name oracle -p 1521:1521 -p 5500:5500 --network currency_network -e ORACLE_PWD=12345678 --shm-size=1g -v <DB_FOLDER>:/docker-entrypoint-initdb.d/startup oracle/database:11.2.0.2-xe` , ahol a <DB_FOLDER> egy abszolút útvonal a forráskód mellett található `db/init` mappára. Az adatbázis indulása hosszabb ideig is eltarthat.
* Forráskód fordítása, futtatható jar elkészítése: `mvn clean install`
* A forráskód gyökérkönyvtárában (ahol a Dockerfile található) a következő parancsot kell kiadni: `docker build -t currency-service .` (fontos a pont a végén)
* Végül az alkalmazás indítása konténerben: `docker run -d --name currency-service -p 8080:8080 --network currency_network currency-service`
* Ezután az alkalmazás elindul a 8080-as porton. A végpontok pontos címét és a REST API használatát a Swagger-es dokumentáció írja le, amely megtalálható futó alkalmazás esetén a [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) oldalon.


## Tesztelés

Az alkalmazás unit és integrációs tesztekkel is rendelkezik, ezeket a szokásos módon IDE-ből, vagy a maven build során is lehet futtatni.
Pl: `mvn clean install`

Ezen kívül, ha fut az alkalmazás, akkor a végpontok tesztelhetőek JMeter-ből is. Ehhez a `currency-service.jmx` fájl használható.