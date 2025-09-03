
CREATE TABLE public.customers (
	customer_id uuid NOT NULL,
	"name" varchar(100) NOT NULL,
	last_name varchar(100) NOT NULL,
	phone varchar(20) NOT NULL,
	email varchar(100) NOT NULL,
	base_salary numeric NULL,
	"document" varchar(30) NOT NULL,
	CONSTRAINT customers_document_key UNIQUE (document),
	CONSTRAINT customers_pkey PRIMARY KEY (customer_id)
);
CREATE UNIQUE INDEX idx_email ON public.customers USING btree (email);