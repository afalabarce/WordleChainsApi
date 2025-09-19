--
-- PostgreSQL database dump
--

-- Dumped from database version 17.6 (Postgres.app)
-- Dumped by pg_dump version 17.5

-- Started on 2025-09-19 12:35:25 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE IF EXISTS wordleapp;
--
-- TOC entry 3822 (class 1262 OID 16390)
-- Name: wordleapp; Type: DATABASE; Schema: -; Owner: wordleapp
--

CREATE DATABASE wordleapp WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'C';


ALTER DATABASE wordleapp OWNER TO wordleapp;

\connect wordleapp

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 16464)
-- Name: api_keys; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.api_keys (
    api_key uuid NOT NULL,
    developer text NOT NULL
);


ALTER TABLE public.api_keys OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16469)
-- Name: countries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.countries (
    country_id character varying(10) NOT NULL,
    country_name character varying(100),
    flag_url character varying(500)
);


ALTER TABLE public.countries OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16474)
-- Name: daily_game; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.daily_game (
    daily_id integer NOT NULL,
    date bigint NOT NULL,
    language character varying(10)
);


ALTER TABLE public.daily_game OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16477)
-- Name: daily_game_daily_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.daily_game_daily_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.daily_game_daily_id_seq OWNER TO postgres;

--
-- TOC entry 3824 (class 0 OID 0)
-- Dependencies: 220
-- Name: daily_game_daily_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.daily_game_daily_id_seq OWNED BY public.daily_game.daily_id;


--
-- TOC entry 221 (class 1259 OID 16478)
-- Name: daily_game_words; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.daily_game_words (
    daily_game_word_id integer NOT NULL,
    daily_id bigint NOT NULL,
    word_id bigint NOT NULL,
    linked_word_id bigint,
    linking_position integer,
    linked_word_position integer
);


ALTER TABLE public.daily_game_words OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16481)
-- Name: daily_game_words_daily_game_word_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.daily_game_words_daily_game_word_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.daily_game_words_daily_game_word_id_seq OWNER TO postgres;

--
-- TOC entry 3825 (class 0 OID 0)
-- Dependencies: 222
-- Name: daily_game_words_daily_game_word_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.daily_game_words_daily_game_word_id_seq OWNED BY public.daily_game_words.daily_game_word_id;


--
-- TOC entry 223 (class 1259 OID 16482)
-- Name: hall_of_fame; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.hall_of_fame (
    id integer NOT NULL,
    player_nick character varying(100),
    device_id character varying(255),
    player_country_id character varying(10),
    last_play_date bigint,
    words_count_1 bigint DEFAULT 0 NOT NULL,
    words_count_2 bigint DEFAULT 0 NOT NULL,
    words_count_3 bigint DEFAULT 0 NOT NULL,
    words_count_4 bigint DEFAULT 0 NOT NULL,
    words_count_5 bigint DEFAULT 0 NOT NULL,
    words_count_6 bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.hall_of_fame OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16491)
-- Name: hall_of_fame_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hall_of_fame_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.hall_of_fame_id_seq OWNER TO postgres;

--
-- TOC entry 3826 (class 0 OID 0)
-- Dependencies: 224
-- Name: hall_of_fame_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.hall_of_fame_id_seq OWNED BY public.hall_of_fame.id;


--
-- TOC entry 225 (class 1259 OID 16492)
-- Name: words; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.words (
    id integer NOT NULL,
    word character varying(5) NOT NULL,
    definition text DEFAULT ''::text NOT NULL,
    language character varying(10)
);


ALTER TABLE public.words OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16498)
-- Name: words_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.words_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.words_id_seq OWNER TO postgres;

--
-- TOC entry 3827 (class 0 OID 0)
-- Dependencies: 226
-- Name: words_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.words_id_seq OWNED BY public.words.id;


--
-- TOC entry 3643 (class 2604 OID 16499)
-- Name: daily_game daily_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_game ALTER COLUMN daily_id SET DEFAULT nextval('public.daily_game_daily_id_seq'::regclass);


--
-- TOC entry 3644 (class 2604 OID 16500)
-- Name: daily_game_words daily_game_word_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_game_words ALTER COLUMN daily_game_word_id SET DEFAULT nextval('public.daily_game_words_daily_game_word_id_seq'::regclass);


--
-- TOC entry 3645 (class 2604 OID 16501)
-- Name: hall_of_fame id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hall_of_fame ALTER COLUMN id SET DEFAULT nextval('public.hall_of_fame_id_seq'::regclass);


--
-- TOC entry 3652 (class 2604 OID 16502)
-- Name: words id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.words ALTER COLUMN id SET DEFAULT nextval('public.words_id_seq'::regclass);


--
-- TOC entry 3663 (class 2606 OID 16542)
-- Name: hall_of_fame UQ_PlayerNick; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hall_of_fame
    ADD CONSTRAINT "UQ_PlayerNick" UNIQUE (player_nick);


--
-- TOC entry 3655 (class 2606 OID 16504)
-- Name: api_keys api_keys_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.api_keys
    ADD CONSTRAINT api_keys_pkey PRIMARY KEY (api_key);


--
-- TOC entry 3657 (class 2606 OID 16506)
-- Name: countries countries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.countries
    ADD CONSTRAINT countries_pkey PRIMARY KEY (country_id);


--
-- TOC entry 3659 (class 2606 OID 16508)
-- Name: daily_game daily_game_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_game
    ADD CONSTRAINT daily_game_pkey PRIMARY KEY (daily_id);


--
-- TOC entry 3665 (class 2606 OID 16510)
-- Name: hall_of_fame hall_of_fame_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hall_of_fame
    ADD CONSTRAINT hall_of_fame_pkey PRIMARY KEY (id);


--
-- TOC entry 3661 (class 2606 OID 16512)
-- Name: daily_game_words pk_daily_game_word; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_game_words
    ADD CONSTRAINT pk_daily_game_word PRIMARY KEY (daily_game_word_id);


--
-- TOC entry 3667 (class 2606 OID 16514)
-- Name: words words_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.words
    ADD CONSTRAINT words_pkey PRIMARY KEY (id);


--
-- TOC entry 3668 (class 2606 OID 16515)
-- Name: daily_game_words fg_daily_game; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_game_words
    ADD CONSTRAINT fg_daily_game FOREIGN KEY (daily_id) REFERENCES public.daily_game(daily_id) ON DELETE CASCADE;


--
-- TOC entry 3669 (class 2606 OID 16520)
-- Name: daily_game_words fg_linking_word; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_game_words
    ADD CONSTRAINT fg_linking_word FOREIGN KEY (linked_word_id) REFERENCES public.words(id) ON DELETE RESTRICT NOT VALID;


--
-- TOC entry 3671 (class 2606 OID 16525)
-- Name: hall_of_fame fg_player_country; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.hall_of_fame
    ADD CONSTRAINT fg_player_country FOREIGN KEY (player_country_id) REFERENCES public.countries(country_id) ON DELETE SET NULL NOT VALID;


--
-- TOC entry 3670 (class 2606 OID 16530)
-- Name: daily_game_words fg_word; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_game_words
    ADD CONSTRAINT fg_word FOREIGN KEY (word_id) REFERENCES public.words(id) ON DELETE RESTRICT NOT VALID;


--
-- TOC entry 3823 (class 0 OID 0)
-- Dependencies: 3822
-- Name: DATABASE wordleapp; Type: ACL; Schema: -; Owner: wordleapp
--

REVOKE ALL ON DATABASE wordleapp FROM wordleapp;
GRANT ALL ON DATABASE wordleapp TO wordleapp WITH GRANT OPTION;


-- Completed on 2025-09-19 12:35:25 CEST

--
-- PostgreSQL database dump complete
--

