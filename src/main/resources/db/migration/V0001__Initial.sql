create sequence athlete_seq start with 1000;

create table athlete
(
    id              bigint       not null default nextval('athlete_seq') primary key,

    first_name      varchar(100) not null,
    last_name       varchar(100) not null,
    gender          char(1)      not null check ( gender in ('f', 'm') ),
    year_of_birth   int          not null check ( year_of_birth > 1900 ),

    club_id         bigint                default null,
    organization_id bigint                default null
);

create sequence category_seq start with 1000;

create table category
(
    id           bigint      not null default nextval('category_seq') primary key,

    abbreviation varchar(10) not null,
    name         varchar(50) not null,
    gender       char(1)     not null,
    year_from    int         not null,
    year_to      int         not null,

    series_id    bigint               default null
);

create table category_athlete
(
    category_id bigint not null,
    athlete_id  bigint not null
);

create table category_event
(
    category_id bigint not null,
    event_id    bigint not null,

    position    int    not null
);

create sequence club_seq start with 1000;

create table club
(
    id              bigint       not null default nextval('club_seq') primary key,

    abbreviation    varchar(10)  not null,
    name            varchar(100) not null,

    organization_id bigint                default null
);

create sequence competition_seq start with 1000;

create table competition
(
    id                        bigint      not null default nextval('competition_seq') primary key,

    name                      varchar(50) not null,
    competition_date          date        not null,
    always_first_three_medals boolean     not null default false,
    medal_percentage          int         not null,
    locked                    boolean     not null default false,

    series_id                 bigint               default null
);

create sequence event_seq start with 1000;

create table event
(
    id              bigint           not null default nextval('event_seq') primary key,

    abbreviation    varchar(10)               default null,
    name            varchar(50)               default null,
    gender          char(1)                   default null,
    event_type      varchar(10)               default null,
    a               double precision not null,
    b               double precision not null,
    c               double precision not null,

    organization_id bigint                    default null
);

create sequence organization_seq start with 1000;

create table organization
(
    id               bigint      not null default nextval('organization_seq') primary key,

    organization_key varchar(10) not null,
    name             varchar(50) not null,
    owner            varchar(50) not null
);

create table organization_user
(
    organization_id bigint not null,
    user_id         bigint not null
);

create sequence result_seq start with 1000;

create table result
(
    id             bigint      not null default nextval('result_seq') primary key,

    position       int         not null,
    result         varchar(20) not null,
    points         int         not null,

    athlete_id     bigint      not null,
    category_id    bigint      not null,
    competition_id bigint      not null,
    event_id       bigint      not null
);

create sequence security_group_seq start with 1000;

create table security_group
(
    id   bigint      not null default nextval('security_group_seq') primary key,

    name varchar(50) not null
);

create sequence security_user_seq start with 1000;

create table security_user
(
    id              bigint       not null default nextval('security_user_seq') primary key,

    first_name      varchar(100) not null,
    last_name       varchar(100) not null,
    email           varchar(50)  not null,
    secret          varchar(100) not null,

    confirmation_id varchar(200),
    confirmed       boolean               default false
);

create sequence series_seq start with 1000;

create table series
(
    id              bigint      not null default nextval('series_seq') primary key,

    name            varchar(50) not null,
    logo            bytea,
    hidden          boolean     not null default false,
    locked          boolean     not null default false,

    organization_id bigint               default null
);

create table user_group
(
    user_id  bigint not null,
    group_id bigint not null
);

alter table athlete
    add constraint fk_athlete_club foreign key (club_id) references club (id);
alter table athlete
    add constraint fk_athlete_organization foreign key (organization_id) references organization (id);

alter table category
    add constraint fk_category_series foreign key (series_id) references series (id);

alter table category_athlete
    add primary key (athlete_id, category_id);
alter table category_athlete
    add constraint fk_category_athlete_athlete foreign key (athlete_id) references athlete (id);
alter table category_athlete
    add constraint fk_category_athlete_category foreign key (category_id) references category (id);

alter table category_event
    add primary key (category_id, event_id);
alter table category_event
    add constraint fk_category_event_category foreign key (category_id) references category (id);
alter table category_event
    add constraint fk_category_event_event foreign key (event_id) references event (id);

alter table club
    add constraint fk_club_organization foreign key (organization_id) references organization (id);

alter table competition
    add constraint fk_competition_series foreign key (series_id) references series (id);

alter table event
    add constraint fk_event_organization foreign key (organization_id) references organization (id);

alter table organization
    add constraint uk_organization_key unique (organization_key);

alter table organization_user
    add primary key (organization_id, user_id);
alter table organization_user
    add constraint fk_organization_user_organization foreign key (organization_id) references organization (id);
alter table organization_user
    add constraint fk_organization_user_user foreign key (user_id) references security_user (id);

alter table result
    add constraint fk_result_athlete foreign key (athlete_id) references athlete (id);
alter table result
    add constraint fk_result_category foreign key (category_id) references category (id);
alter table result
    add constraint fk_result_competition foreign key (competition_id) references competition (id);
alter table result
    add constraint fk_result_event foreign key (event_id) references event (id);

alter table security_group
    add constraint uk_security_group_name unique (name);
alter table security_user
    add constraint uk_security_user_email unique (email);

alter table series
    add constraint fk_series_organization foreign key (organization_id) references organization (id);

alter table user_group
    add primary key (group_id, user_id);
alter table user_group
    add constraint fk_user_group_user foreign key (user_id) references security_user (id);
alter table user_group
    add constraint fk_user_group_group foreign key (group_id) references security_group (id);
