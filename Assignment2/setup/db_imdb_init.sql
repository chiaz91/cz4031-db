-- drop previous created tables if exists
DROP TABLE IF EXISTS titles;
DROP TABLE IF EXISTS ratings;

-- -- 1. create titles table
create table titles
(
	tconst character varying(20),
	titleType character varying(20),
	primaryTitle character varying(500),
	originalTitle character varying(500),
	isAdult character varying(10),
	startYear character varying(10), 
	endYear character varying(20),
	runtimeMinutes character varying(20),
	genres character varying(400)
);

-- 2. create ratings table
create table ratings 
(
	tconst character varying(20),
	averageRating character varying(20),
	numVotes character varying(20)
);