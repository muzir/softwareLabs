select * from posts limit 10;

-- counter,  owneruserid
-- 45197	1144035
-- 34347	22656
-- 21480	29407
-- 21122	548225
-- 20573	115145

select count(1) counter, owneruserid from posts group by 2 order by counter desc;


-- counter,  tags

-- 100251	"<android>"
-- 83317	"<javascript>"
-- 80414	"<javascript><jquery>"
-- 80210	"<java>"
-- 78661	"<php>"
-- 74473	"<html><css>"
-- 66010	"<python>"
-- 55211	"<jquery>"
-- 54834	"<c#>"
-- 49863	"<php><mysql>"

select count(1) counter, tags from posts where tags is not null group by 2 order by counter desc;


-- counter,  posttypeid
-- 24489327	2
-- 15468906	1
-- 46641	5
-- 46641	4
-- 312	6
-- 167	3
-- 4	7
-- 2	8

select count(1) counter, posttypeid from posts group by 2 order by counter desc;

-- Sum score of the user in tags and posttypeid between a time range creationdate

create index CONCURRENTLY IF NOT EXISTS idx_owneruserid_posttypeid_tags_creationdate on posts(owneruserid, posttypeid, tags, creationdate);

create index CONCURRENTLY IF NOT EXISTS idx_owneruserid_posttypeid_score on posts(owneruserid, posttypeid, score);

select count(1), EXTRACT(YEAR FROM  creationdate) from posts where owneruserid = 1144035 group by 2;

select count(1), posttypeid, tags from posts where owneruserid = 1144035 group by 2,3;

-- 1144035
explain analyse select sum(score) from posts where owneruserid = 1144035 and posttypeid = 2

    explain analyse select count(1) from posts where owneruserid = 1144035 and posttypeid = 2

select count(1) counter, EXTRACT(YEAR FROM  creationdate), tags, owneruserid, posttypeid
from posts where tags is not null
group by 2,3, 4,5 order by counter desc;

explain analyse select sum(score) from posts where owneruserid = 149080 and posttypeid = 1 and tags = '<ruby-on-rails><ruby-on-rails-3>' and creationdate > '2011-01-01 00:00:00'

select sum(score) from posts where owneruserid = 149080 and posttypeid = 1 and tags = '<ruby-on-rails><ruby-on-rails-3>' and creationdate > '2011-01-01 00:00:00'

select count(1) from posts where owneruserid = 149080 and posttypeid = 1 and tags = '<ruby-on-rails><ruby-on-rails-3>' and creationdate > '2011-01-01 00:00:00'

select EXTRACT(YEAR FROM  TIMESTAMP  '2022-01-01 00:00:00')
