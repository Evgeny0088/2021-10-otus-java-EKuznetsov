ДЗ: hw21-jpql<br>
1. На практике освоить основы Hibernate.
   Понять как аннотации-hibernate влияют на формирование sql-запросов.

2. Работа должна использовать базу данных в docker-контейнере .

3. За основу возьмите пример из вебинара про JPQL (class DbServiceDemo).
   Добавьте в Client поля:
   адрес  (OneToOne)
   class Address {
   private String street;
   }
   и телефон (OneToMany)
   class Phone {
   private String number;
   }

4. Разметьте классы таким образом, чтобы при сохранении/чтении объека Client каскадно сохранялись/читались вложенные объекты.
