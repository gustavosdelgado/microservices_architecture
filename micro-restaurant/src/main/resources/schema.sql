create table if not exists restaurant(

    id bigint not null auto_increment,
    name varchar(255) not null,
    user varchar(255) not null,

    primary key(id)

);