DO $$
BEGIN
FOR client_id in 1..10
    loop
        insert into client(id,name) values (client_id,concat('client name',client_id));
    end loop;
END $$;
