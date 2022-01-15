#!/bin/sh
docker cp name.basics.tsv my-postgres:/tmp
echo "name.basics.tsv copied"
docker cp title.akas.tsv my-postgres:/tmp
echo "title.akas.tsv copied"
docker cp title.basics.tsv my-postgres:/tmp
echo "title.basics.tsv copied"
docker cp title.crew.tsv my-postgres:/tmp
echo "title.crew.tsv copied"
docker cp title.episode.tsv my-postgres:/tmp
echo "title.episode.tsv copied"
docker cp title.principals.tsv my-postgres:/tmp
echo "title.principals.tsv copied"
docker cp title.ratings.tsv my-postgres:/tmp
echo "title.ratings.tsv copied"
