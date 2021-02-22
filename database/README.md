```shell
$ sudo docker-compose up -d
$ sudo docker-compose restart
```

```shell
$ mysql -u root -h 127.0.0.1 -p classicmodels --password=admin
$ mysql -u root -h 127.0.0.1 -p results --password=admin
```

```sql
SELECT sql_text FROM mysql.slow_log order by start_time desc limit 1;
```

---
1. Click File from the toolbar 
1. Select Project Structure option
1. Select Modules at the left panel
1. Select Dependencies tab
1. Select + icon
1. Select 1 JARs or directories option
