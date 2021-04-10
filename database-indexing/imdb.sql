-- \COPY temp_name_basics FROM '/tmp/name.basics.tsv' DELIMITER E'\t' CSV HEADER QUOTE E'\b' NULL AS '';
-- \COPY temp_title_akas FROM '/tmp/title.akas.tsv' DELIMITER E'\t' CSV HEADER QUOTE E'\b' NULL AS '';
-- \COPY temp_title_crew FROM '/tmp/title.crew.tsv' DELIMITER E'\t' CSV HEADER QUOTE E'\b' NULL AS '';
-- \COPY temp_title_episode FROM '/tmp/title.episode.tsv' DELIMITER E'\t' CSV HEADER QUOTE E'\b' NULL AS '';
-- \COPY temp_title_principals FROM '/tmp/title.principals.tsv' DELIMITER E'\t' CSV HEADER QUOTE E'\b' NULL AS '';
-- \COPY temp_title_ratings FROM '/tmp/title.ratings.tsv' DELIMITER E'\t' CSV HEADER QUOTE E'\b' NULL AS '';
-- \COPY temp_title_basics FROM '/tmp/title.basics.tsv' DELIMITER E'\t' CSV HEADER QUOTE E'\b' NULL AS '';


create database imdb;

-- temp tables
-- temp_name.basics
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

-- temp_title_akas
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


-- title_crew
drop table if exists temp_title_crew;
create table temp_title_crew
(
    tconst    text,
    directors text,
    writers   text
);

-- title_episode
drop table if exists temp_title_episode;
create table temp_title_episode
(
    tconst        text,
    parentTconst  text,
    seasonNumber  text,
    episodeNumber text
);

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

-- title_ratings
drop table if exists temp_title_ratings;
create table temp_title_ratings
(
    tconst        text,
    averageRating decimal,
    numVotes      integer
);


-- title_basics
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

-- Main tables

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
       string_to_array(tnb.primaryProfession, ','),
       string_to_array(tnb.knownForTitles, ',')
from temp_name_basics tnb;

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
into
    title_akas (titleId,
                ordering,
                title,
                region,
                language,
                types ,
                attributes,
                isOriginalTitle)
select
    tta.titleId,
    tta.ordering,
    tta.title,
    tta.region,
    tta.language,
    string_to_array(tta.types, ','),
    string_to_array(tta.attributes, ','),
    case
        when tta.isOriginalTitle = '\N' then null
        else tta.isOriginalTitle :: boolean
        end
from
    temp_title_akas tta;


-- References
-- How to install and run postgres in a docker container https://betterprogramming.pub/connect-from-local-machine-to-postgresql-docker-container-f785f00461a7
-- Where to download IMDB datasets https://datasets.imdbws.com/
-- Extract.gz files https://stackoverflow.com/a/50996888/1577363
-- Copy files from local to docker container https://support.sitecore.com/kb?id=kb_article_view&sysparm_article=KB0383441
--  - docker cp name.basics.tsv my-postgres:tmp
-- Copy data from tsv files to postgres table /COPY command usage, turn off quote processing https://stackoverflow.com/a/20402913/1577363, https://www.postgresql.org/docs/current/sql-copy.html
--


