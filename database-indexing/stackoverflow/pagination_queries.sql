/*
Top 10 users with the most posts
929_455,0
85_089,1144035
37_940,3732271
35761,22656
33003,1491895
31252,6309
31016,2901002
25821,548225
24757,19068
23140,115145
*/
select count(1), owner_user_id
from posts
group by owner_user_id
order by 1 desc;
-- Top 10
/*506855,0,1
421588,0,2
85089,1144035,2
37940,3732271,2
35697,22656,2
32957,1491895,2
31232,6309,2
30961,2901002,2
25808,548225,2
24749,19068,2
*/
select count(1), owner_user_id, post_type_id
from posts
group by owner_user_id, post_type_id
order by 1 desc;

-- Classic pagination query before index creation
explain analyse
select *
from posts
where owner_user_id = 0
  and post_type_id != 2
order by creation_date desc;
--offset 0 limit 10000;


drop index concurrently if exists idx_posts_owner_user_id_creation_date;
create index concurrently if not exists idx_posts_owner_user_id_creation_date on posts (owner_user_id, creation_date);

drop index concurrently if exists idx_posts_owner_user_id_creation_date_post_type_id;
create index concurrently if not exists idx_posts_owner_user_id_creation_date_post_type_id on posts (owner_user_id, creation_date) include (post_type_id);