create table fiscal_years
(
    fiscal_year INTEGER,
    start_date  DATE,
    end_date    DATE
);

create table fiscal_years_valid
(
    fiscal_year INTEGER NOT NULL PRIMARY KEY,
    start_date  DATE    NOT NULL,
    CONSTRAINT valid_start_date CHECK ( (extract(year from start_date) = fiscal_year - 1)
    AND (extract(month from start_date) = 10)
    AND (extract(day from start_date) = 1)),
    end_date    DATE    NOT NULL
        CONSTRAINT valid_end_date CHECK ( (extract(year from end_date) = fiscal_year)
            AND (extract(month from end_date) = 9)
            AND (extract(day from end_date) = 30))
    );


INSERT INTO fiscal_years_valid (fiscal_year, start_date, end_date)
VALUES (2020, '2020-10-01', '2020-09-30'),
       (2021, '2021-10-01', '2021-09-30'),
       (2022, '2022-10-01', '2022-09-30'),
       (2023, '2023-10-01', '2023-09-30'),
       (2024, '2024-10-01', '2024-09-30'),
       (2025, '2025-10-01', '2025-09-30');

select *
from fiscal_years;

