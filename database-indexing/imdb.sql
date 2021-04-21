create database imdb;

-- create name_basics
drop table if exists temp_name_basics;
create table temp_name_basics
(
    nconst            text,
    primaryName       text,
    birthYear         text,
    deathYear         text,
    primaryProfession text,
    knownForTitles    text
);

COPY temp_name_basics FROM '/tmp/name.basics.tsv' DELIMITER E'\t' CSV HEADER QUOTE E'\b' NULL AS '';

drop table if exists name_basics;
create table name_basics
(
    nconst            text,
    primaryName       text,
    birthYear         integer,
    deathYear         integer,
    primaryProfession text[],
    knownForTitles    text[]
);

insert into name_basics (nconst, primaryName, birthYear, deathYear, primaryProfession, knownForTitles)
select tnb.nconst,
       tnb.primaryName,
       CASE WHEN tnb.birthYear = '\N' THEN NULL ELSE tnb.birthYear :: Integer END,
       CASE WHEN tnb.deathYear = '\N' THEN NULL ELSE tnb.deathYear :: Integer END,
       CASE WHEN tnb.primaryProfession = '\N' THEN NULL ELSE string_to_array(tnb.primaryProfession, ',') END,
       CASE WHEN tnb.knownForTitles = '\N' THEN NULL ELSE string_to_array(tnb.knownForTitles, ',') END
from temp_name_basics tnb;

drop table if exists temp_name_basics;


-- create title_akas

drop table if exists temp_title_akas;
create table temp_title_akas
(
    titleId         text,
    ordering        integer,
    title           text,
    region          text,
    language        text,
    types           text,
    attributes      text,
    isOriginalTitle text
);

COPY temp_title_akas FROM '/tmp/title.akas.tsv' DELIMITER E'\t' CSV HEADER QUOTE E'\b' NULL AS '';

drop table if exists title_akas;
create table title_akas
(
    titleId         text,
    ordering        integer,
    title           text,
    region          text,
    language        text,
    types           text[],
    attributes      text[],
    isOriginalTitle boolean
);

insert
into title_akas (titleId,
                 ordering,
                 title,
                 region,
                 language,
                 types,
                 attributes,
                 isOriginalTitle)
select tta.titleId,
       tta.ordering,
       tta.title,
       tta.region,
       case
           when tta.language = '\N' then null
           else tta.language
           end,
       case
           when tta.types = '\N' then null
           else string_to_array(tta.types, ',')
           end,
       case
           when tta.attributes = '\N' then null
           else string_to_array(tta.attributes, ',')
           end,
       case
           when tta.isOriginalTitle = '\N' then null
           else tta.isOriginalTitle :: boolean
           end
from temp_title_akas tta;

drop table if exists temp_title_akas;

-- create title_basics

drop table if exists temp_title_basics;
create table temp_title_basics
(
    tconst         text,
    titleType      text,
    primaryTitle   text,
    originalTitle  text,
    isAdult        boolean,
    startYear      text,
    endYear        text,
    runtimeMinutes text,
    genres         text
);

COPY temp_title_basics FROM '/tmp/title.basics.tsv' DELIMITER E'\t' CSV HEADER QUOTE E'\b' NULL AS '';

drop table if exists title_basics;
create table title_basics
(
    tconst         text,
    titleType      text,
    primaryTitle   text,
    originalTitle  text,
    isAdult        boolean,
    startYear      integer,
    endYear        integer,
    runtimeMinutes integer,
    genres         text[]
);

insert
into title_basics(tconst,
                  titleType,
                  primaryTitle,
                  originalTitle,
                  isAdult,
                  startYear,
                  endYear,
                  runtimeMinutes,
                  genres)
select ttb.tconst,
       ttb.titletype,
       ttb.primarytitle,
       ttb.originaltitle,
       ttb.isadult,
       case
           when ttb.startyear = '\N' then null
           else ttb.startyear :: Integer
           end,
       case
           when ttb.endyear = '\N' then null
           else ttb.endyear :: Integer
           end,
       case
           when ttb.runtimeMinutes = '\N' then null
           else ttb.runtimeMinutes :: Integer
           end,
       string_to_array(ttb.genres, ',')
from temp_title_basics ttb;

drop table if exists temp_title_basics;

-- title_crew
drop table if exists temp_title_crew;
create table temp_title_crew
(
    tconst    text,
    directors text,
    writers   text
);

COPY temp_title_crew FROM '/tmp/title.crew.tsv' DELIMITER E'\t' CSV HEADER QUOTE E'\b' NULL AS '';

drop table if exists title_crew;
create table title_crew
(
    tconst    text,
    directors text[],
    writers   text[]
);

insert into title_crew (tconst, directors, writers)
select ttc.tconst,
       string_to_array(ttc.directors, ','),
       string_to_array(ttc.writers, ',')
from temp_title_crew ttc;

drop table if exists temp_title_crew;


-- title_episode

drop table if exists temp_title_episode;

create table temp_title_episode
(
    tconst        text,
    parentTconst  text,
    seasonNumber  text,
    episodeNumber text
);

copy temp_title_episode
    from
    '/tmp/title.episode.tsv' delimiter E'\t' csv header quote E'\b' null as '';

drop table if exists title_episode;

create table title_episode
(
    tconst        text,
    parentTconst  text,
    seasonNumber  integer,
    episodeNumber integer
);

insert
into title_episode (tconst,
                    parentTconst,
                    seasonNumber,
                    episodeNumber)
select tte.tconst,
       tte.parentTconst,
       case
           when tte.seasonNumber = '\N' then null
           else tte.seasonNumber :: Integer
           end,
       case
           when tte.episodeNumber = '\N' then null
           else tte.episodeNumber :: Integer
           end
from temp_title_episode tte;

drop table if exists temp_title_episode;

-- title_principals
drop table if exists temp_title_principals;

create table temp_title_principals
(
    tconst     text,
    ordering   integer,
    nconst     text,
    category   text,
    job        text,
    characters text
);

copy temp_title_principals
    from
    '/tmp/title.principals.tsv' delimiter E'\t' csv header quote E'\b' null as '';

drop table if exists title_principals;
create table title_principals
(
    tconst     text,
    ordering   integer,
    nconst     text,
    category   text,
    job        text,
    characters text
);

insert
into title_principals (tconst,
                       ordering,
                       nconst,
                       category,
                       job,
                       characters)
select ttp.tconst,
       ttp.ordering,
       ttp.nconst,
       ttp.category,
       case
           when ttp.job = '\N' then null
           else ttp.job
           end,
       case
           when ttp."characters" = '\N' then null
           else ttp."characters"
           end
from temp_title_principals ttp;

drop table if exists temp_title_principals;

-- title_ratings
drop table if exists temp_title_ratings;
create table temp_title_ratings
(
    tconst        text,
    averageRating text,
    numVotes      text
);

copy temp_title_ratings
    from
    '/tmp/title.ratings.tsv' delimiter E'\t' csv header quote E'\b' null as '';

drop table if exists title_ratings;
create table title_ratings
(
    tconst        text,
    averageRating decimal,
    numVotes      integer
);

insert
into title_ratings (tconst,
                    averageRating,
                    numVotes)
select ttr.tconst,
       case
           when ttr.averageRating = '\N' then null
           else ttr.averageRating :: decimal
           end,
       case
           when ttr.numVotes = '\N' then null
           else ttr.numVotes :: Integer
           end
from temp_title_ratings ttr;

drop table if exists temp_title_ratings;


-- References
-- How to install and run postgres in a docker container https://betterprogramming.pub/connect-from-local-machine-to-postgresql-docker-container-f785f00461a7
-- docker start my-postgres
-- Where to download IMDB datasets https://datasets.imdbws.com/
-- Extract.gz files https://stackoverflow.com/a/50996888/1577363
-- Copy files from local to docker container https://support.sitecore.com/kb?id=kb_article_view&sysparm_article=KB0383441
--  - docker cp name.basics.tsv my-postgres:tmp
-- Copy data from tsv files to postgres table /COPY command usage, turn off quote processing https://stackoverflow.com/a/20402913/1577363, https://www.postgresql.org/docs/current/sql-copy.html
