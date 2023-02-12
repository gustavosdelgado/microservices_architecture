create table if not exists users(

    id bigint not null auto_increment,
    login varchar(100) not null,
    password varchar(255) not null,
    role varchar(255),

    primary key(id)

);