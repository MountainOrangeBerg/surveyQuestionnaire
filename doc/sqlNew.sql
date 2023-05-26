CREATE TABLE admin_org (
                           easdept_id VARCHAR(255),
                           fnumber VARCHAR(255),
                           name VARCHAR(255),
                           status INT,
                           superior VARCHAR(255),
                           supname VARCHAR(255),
                           supFnumber VARCHAR(255),
                           endflag INT,
                           sortCode VARCHAR(255),
                           fEffectDate DATETIME,
                           flastUpdateTime DATETIME,
                           fcreateTime DATETIME,
                           PRIMARY KEY (easdept_id)
);

create table person
(
    easuser_id      varchar(255) not null
        primary key,
    username        varchar(255) null,
    loginid         varchar(255) null,
    fnumber         varchar(255) null,
    descn           varchar(255) null,
    sex             int          null,
    birthday        date         null,
    email           varchar(255) null,
    address         varchar(255) null,
    officephone     varchar(255) null,
    dept_id         varchar(255) null,
    eas_loginid     varchar(255) null,
    homephone       varchar(255) null,
    mobile          decimal(11)  null,
    fcreateTime     datetime     null,
    fLastUpdateTime datetime     null,
    enterDate       date         null,
    org_number      varchar(255) null
);

CREATE TABLE position (
                          eas_id VARCHAR(255),
                          fnumber VARCHAR(255),
                          name VARCHAR(255),
                          descn VARCHAR(255),
                          isrespposition INT,
                          parent_id VARCHAR(255),
                          dept_id VARCHAR(255),
                          positiontype_id VARCHAR(255),
                          positiontype_name VARCHAR(255),
                          deletedstatus INT,
                          effectdate DATE,
                          fcreateTime DATETIME,
                          fLastUpdateTime DATETIME,
                          PRIMARY KEY (eas_id)
);
