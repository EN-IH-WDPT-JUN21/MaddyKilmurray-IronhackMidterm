# MaddyKilmurray-IronhackMidterm

Data for database: 

INSERT INTO user (dtype,name,password,username,date_of_birth,mailing_city,mailing_country,mailing_house_number,mailing_postcode,mailing_street_name,primary_city,primary_country,primary_house_number,primary_postcode,primary_street_name,hashed_key) VALUES
('ADMIN','Maddy','1adminPass','admin1',null,null,null,null,null,null,null,null,null,null,null,null),
('ADMIN','Eddi','2adminPass','admin2',null,null,null,null,null,null,null,null,null,null,null,null),
('THIRDPARTY','Credit Score Inc','cs1nc','csInc',null,null,null,null,null,null,null,null,null,null,null,'$2a$12$5eSfdMhLPH66VykW75xYreGIoeqNx5pJhcRAyeAT0B8XTGDcZTECC'),
('THIRDPARTY','Money Checker','checkMon1','checkMon',null,null,null,null,null,null,null,null,null,null,null,'$2a$12$gGIELJ9TTO7TAFHl4RJH6.48Hz98PpIWkmfCGMairQZFKeqAHkVIy'),
('THIRDPARTY','Mortgages R Us','mortMort','mortRus',null,null,null,null,null,null,null,null,null,null,null,'$2a$12$3v1TILLURdlYrmd8BvO3ROcUEwk6WtgfLE/wWSyOPcMJryff5QOFS'),
('ACCOUNTHOLDER','Ted Bundy','pumpkinHead','TedBundy','1946-11-24','Burlington','USA','123','6114-6114','Psycho Street',null,null,null,null,null,null),
('ACCOUNTHOLDER','Jeffry Dahmer','milwaukee','Dahmer','1960-05-21','Milwaukee','USA','123','6114-6114','Monster Avenue',null,null,null,null,null,null),
('ACCOUNTHOLDER','Aileen Wuornos','pointBl@nk','Aileen','1956-02-29','Rochester','USA','123','6114-6114','Shooter Road','Florida','USA','456','511-45','Shot Court',null),
('ACCOUNTHOLDER','Charles Manson','cultLeader','Manson','1934-11-12','Cincinnati','USA','123','6114-6114','Brainwash Drive','Bakersfield','USA','999','511-45','Shot Court',null),
('ACCOUNTHOLDER','John Wayne Gacy','clown','KillerClown','1942-03-17','Chicago','USA','123','6114-6114','Pogo parade','Crest Hill','USA','999','511-45','Stateville',null),
('ACCOUNTHOLDER','Jack The Ripper','ripper','Jack','1888-06-06','London','UK','123','6114-6114','Whitechapel Lane','London','UK','999','511-45','Spitalfields Spur',null),
('ACCOUNTHOLDER','Andrei Chikatilo','butcher','Rostov','1936-10-16','Yabluchne','Ukraine','123','6114-6114','Red Ripper Road','Novocherkassk','Russia','999','511-45','Chikatilo Chase',null),
('ACCOUNTHOLDER','Ed Gein','ghoul','PlainfieldGhoul','1906-08-27','La Crosse','USA','123','6114-6114','Snatcher street','Madison','USA','999','511-45','Mendota',null);

INSERT INTO account (ACCOUNT_TYPE,BALANCE,BALANCE_CURRENCY,CREATION_DATE,PENALTY_FEE,PENALTY_FEE_CURRENCY,STATUS,MINIMUM_BALANCE,MINIMUM_BALANCE_CURRENCY,MONTHLY_MAINTENANCE_FEE,MAINTENANCE_FEE_CURRENCY,SECRET_KEY,CREDIT_LIMIT,CREDIT_LIMIT_CURRENCY,INTEREST_RATE,INTEREST_RATE_CURRENCY,HASHED_KEY,NAME,PRIMARY_OWNER_ID,SECONDARY_OWNER_ID) VALUES
('CHECKING','50','GBP','2021-06-01','40','GBP','ACTIVE','250','GBP','12','GBP','BERRY','0','GBP','0','GBP','null','null','6','13'),
('CHECKING','18000','GBP','1998-12-13','40','GBP','ACTIVE','250','GBP','12','GBP','BANANA','0','GBP','0','GBP','null','null','7','12'),
('CHECKING','2000','GBP','2001-05-30','40','GBP','ACTIVE','250','GBP','12','GBP','CHERRY','0','GBP','0','GBP','null','null','8','11'),
('STUDENT_CHECKING','5','GBP','2021-06-01','40','GBP','ACTIVE','0','GBP','0','GBP','KIWI','0','GBP','0','GBP','null','null','9','10'),
('STUDENT_CHECKING','200','GBP','1998-12-13','40','GBP','ACTIVE','0','GBP','0','GBP','PAPAYA','0','GBP','0','GBP','null','null','10','9'),
('STUDENT_CHECKING','999','GBP','2001-05-30','40','GBP','ACTIVE','0','GBP','0','GBP','PINEAPPLE','0','GBP','0','GBP','null','null','11','8'),
('CREDIT_CARD','100','GBP','2021-06-01','40','GBP','ACTIVE','0','GBP','0','GBP','MANGO','100','GBP','0','GBP','null','null','12','7'),
('CREDIT_CARD','10000','GBP','1998-12-13','40','GBP','ACTIVE','0','GBP','0','GBP','APPLE','8000','GBP','0','GBP','null','null','13','6'),
('CREDIT_CARD','555555','GBP','2001-05-30','40','GBP','ACTIVE','0','GBP','0','GBP','CLEMENTINE','100000','GBP','0','GBP','null','null','6','13'),
('SAVINGS','100','GBP','2021-06-01','40','GBP','ACTIVE','1000','GBP','0','GBP','ORANGE','0','GBP','0.15','GBP','null','null','7','12'),
('SAVINGS','50000','GBP','1998-12-13','40','GBP','ACTIVE','1000','GBP','0','GBP','GRAPE','0','GBP','0.2','GBP','null','null','8','11'),
('SAVINGS','8000','GBP','2001-05-30','40','GBP','ACTIVE','1000','GBP','0','GBP','TOMATO','0','GBP','0.1','GBP','null','null','9','10'),
('THIRD_PARTY','100','GBP','2021-06-01','40','GBP','ACTIVE','0','GBP','0','GBP','null','0','GBP','0','GBP','$2a$12$5L/mzgG42FemI5yAWk9aEepxwrHKGMXQ9XwxumqkT8uglJuEZ9Vv.','Credit Score Inc','3','2'),
('THIRD_PARTY','500','GBP','1998-12-13','40','GBP','ACTIVE','0','GBP','0','GBP','null','0','GBP','0','GBP','$2a$12$CbUD31J3hhtmsg/eJUVjD.YLzcDv9xSEkmwVBXv/R20age9xCx4hq','Money Checker','4','3'),
('THIRD_PARTY','800','GBP','2001-05-30','40','GBP','ACTIVE','0','GBP','0','GBP','null','0','GBP','0','GBP','$2a$12$SkWl6oche6lu2rvaZpC/Xe/6ttuxxxJx9XKWQ4MBu72oTYNdTHxz6','Mortgages R Us','5','6');
