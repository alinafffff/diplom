--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3
-- Dumped by pg_dump version 16.3

-- Started on 2025-06-05 12:04:31

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4962 (class 1262 OID 25055)
-- Name: inpit; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE inpit WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';


ALTER DATABASE inpit OWNER TO postgres;

\connect inpit

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 915 (class 1247 OID 25324)
-- Name: event_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.event_type AS ENUM (
    'хакатон',
    'волонтерство',
    'хакатон_от_партнера'
);


ALTER TYPE public.event_type OWNER TO postgres;

--
-- TOC entry 894 (class 1247 OID 25200)
-- Name: my_event_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.my_event_type AS ENUM (
    'хакатон',
    'волонтерство',
    'хакатон от партнера'
);


ALTER TYPE public.my_event_type OWNER TO postgres;

--
-- TOC entry 891 (class 1247 OID 25195)
-- Name: notification_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.notification_type AS ENUM (
    'система',
    'новость',
    'запрос',
    'студсовет',
    'результаты'
);


ALTER TYPE public.notification_type OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 222 (class 1259 OID 25086)
-- Name: direction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.direction (
    id integer NOT NULL,
    name character varying NOT NULL,
    abbreviation character varying NOT NULL
);


ALTER TABLE public.direction OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 25085)
-- Name: direction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.direction_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.direction_id_seq OWNER TO postgres;

--
-- TOC entry 4963 (class 0 OID 0)
-- Dependencies: 221
-- Name: direction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.direction_id_seq OWNED BY public.direction.id;


--
-- TOC entry 218 (class 1259 OID 25068)
-- Name: form; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.form (
    id integer NOT NULL,
    name character varying NOT NULL,
    abbreviation character varying
);


ALTER TABLE public.form OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 25067)
-- Name: form_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.form_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.form_id_seq OWNER TO postgres;

--
-- TOC entry 4964 (class 0 OID 0)
-- Dependencies: 217
-- Name: form_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.form_id_seq OWNED BY public.form.id;


--
-- TOC entry 234 (class 1259 OID 25228)
-- Name: my_event; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.my_event (
    id integer NOT NULL,
    name character varying NOT NULL,
    description text NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone,
    type public.event_type,
    created_by integer NOT NULL,
    points integer,
    points_1st integer,
    points_2nd integer,
    points_3rd integer,
    points_participation integer,
    photo_url character varying,
    max_participants integer,
    max_team_size integer,
    is_student_council_request boolean DEFAULT false,
    is_rejected boolean,
    registration_link character varying(255)
);


ALTER TABLE public.my_event OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 25227)
-- Name: my_event_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.my_event_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.my_event_id_seq OWNER TO postgres;

--
-- TOC entry 4965 (class 0 OID 0)
-- Dependencies: 233
-- Name: my_event_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.my_event_id_seq OWNED BY public.my_event.id;


--
-- TOC entry 228 (class 1259 OID 25129)
-- Name: my_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.my_group (
    id integer NOT NULL,
    profile_id integer,
    form_id integer,
    my_level_id integer NOT NULL,
    duration integer NOT NULL,
    number integer NOT NULL,
    leader_id integer,
    organizer_id integer,
    curator_id integer,
    description text,
    start_date date NOT NULL,
    direction_id integer NOT NULL
);


ALTER TABLE public.my_group OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 25128)
-- Name: my_group_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.my_group_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.my_group_id_seq OWNER TO postgres;

--
-- TOC entry 4966 (class 0 OID 0)
-- Dependencies: 227
-- Name: my_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.my_group_id_seq OWNED BY public.my_group.id;


--
-- TOC entry 236 (class 1259 OID 25244)
-- Name: my_group_my_event; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.my_group_my_event (
    id integer NOT NULL,
    my_group_id integer NOT NULL,
    my_event_id integer NOT NULL
);


ALTER TABLE public.my_group_my_event OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 25243)
-- Name: my_group_my_event_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.my_group_my_event_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.my_group_my_event_id_seq OWNER TO postgres;

--
-- TOC entry 4967 (class 0 OID 0)
-- Dependencies: 235
-- Name: my_group_my_event_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.my_group_my_event_id_seq OWNED BY public.my_group_my_event.id;


--
-- TOC entry 242 (class 1259 OID 25294)
-- Name: my_group_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.my_group_requests (
    id integer NOT NULL,
    my_user_id integer NOT NULL,
    my_group_id integer NOT NULL
);


ALTER TABLE public.my_group_requests OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 25293)
-- Name: my_group_requests_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.my_group_requests_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.my_group_requests_id_seq OWNER TO postgres;

--
-- TOC entry 4968 (class 0 OID 0)
-- Dependencies: 241
-- Name: my_group_requests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.my_group_requests_id_seq OWNED BY public.my_group_requests.id;


--
-- TOC entry 220 (class 1259 OID 25077)
-- Name: my_level; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.my_level (
    id integer NOT NULL,
    name character varying NOT NULL,
    abbreviation character varying NOT NULL
);


ALTER TABLE public.my_level OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 25076)
-- Name: my_level_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.my_level_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.my_level_id_seq OWNER TO postgres;

--
-- TOC entry 4969 (class 0 OID 0)
-- Dependencies: 219
-- Name: my_level_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.my_level_id_seq OWNED BY public.my_level.id;


--
-- TOC entry 216 (class 1259 OID 25057)
-- Name: my_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.my_role (
    id integer NOT NULL,
    name character varying NOT NULL
);


ALTER TABLE public.my_role OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 25056)
-- Name: my_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.my_role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.my_role_id_seq OWNER TO postgres;

--
-- TOC entry 4970 (class 0 OID 0)
-- Dependencies: 215
-- Name: my_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.my_role_id_seq OWNED BY public.my_role.id;


--
-- TOC entry 226 (class 1259 OID 25109)
-- Name: my_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.my_user (
    id integer NOT NULL,
    first_name character varying,
    last_name character varying,
    patronymic character varying,
    email character varying NOT NULL,
    login character varying NOT NULL,
    password character varying NOT NULL,
    phone character varying,
    photo_url character varying,
    points integer DEFAULT 0,
    is_blocked boolean DEFAULT false,
    my_role_id integer NOT NULL,
    my_group_id integer
);


ALTER TABLE public.my_user OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 25108)
-- Name: my_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.my_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.my_user_id_seq OWNER TO postgres;

--
-- TOC entry 4971 (class 0 OID 0)
-- Dependencies: 225
-- Name: my_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.my_user_id_seq OWNED BY public.my_user.id;


--
-- TOC entry 230 (class 1259 OID 25173)
-- Name: news; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.news (
    id integer NOT NULL,
    title character varying NOT NULL,
    content text NOT NULL,
    author_id integer NOT NULL,
    my_group_id integer,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    photo_url character varying,
    is_student_council_request boolean DEFAULT false,
    is_rejected boolean
);


ALTER TABLE public.news OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 25172)
-- Name: news_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.news_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.news_id_seq OWNER TO postgres;

--
-- TOC entry 4972 (class 0 OID 0)
-- Dependencies: 229
-- Name: news_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.news_id_seq OWNED BY public.news.id;


--
-- TOC entry 232 (class 1259 OID 25208)
-- Name: notifications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notifications (
    id integer NOT NULL,
    type public.notification_type,
    text text NOT NULL,
    created_at timestamp without time zone DEFAULT now(),
    my_group_id integer,
    reciever_id integer,
    is_read boolean DEFAULT false NOT NULL
);


ALTER TABLE public.notifications OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 25207)
-- Name: notifications_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.notifications_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.notifications_id_seq OWNER TO postgres;

--
-- TOC entry 4973 (class 0 OID 0)
-- Dependencies: 231
-- Name: notifications_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.notifications_id_seq OWNED BY public.notifications.id;


--
-- TOC entry 224 (class 1259 OID 25095)
-- Name: profile; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.profile (
    id integer NOT NULL,
    name character varying NOT NULL,
    direction_id integer NOT NULL,
    number integer NOT NULL
);


ALTER TABLE public.profile OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 25094)
-- Name: profile_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.profile_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.profile_id_seq OWNER TO postgres;

--
-- TOC entry 4974 (class 0 OID 0)
-- Dependencies: 223
-- Name: profile_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.profile_id_seq OWNED BY public.profile.id;


--
-- TOC entry 238 (class 1259 OID 25261)
-- Name: team; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.team (
    id integer NOT NULL,
    name character varying NOT NULL,
    my_event_id integer NOT NULL,
    place integer,
    diploma character varying,
    is_confirmed boolean,
    CONSTRAINT check_place CHECK (((place >= 1) AND (place <= 3)))
);


ALTER TABLE public.team OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 25260)
-- Name: team_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.team_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.team_id_seq OWNER TO postgres;

--
-- TOC entry 4975 (class 0 OID 0)
-- Dependencies: 237
-- Name: team_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.team_id_seq OWNED BY public.team.id;


--
-- TOC entry 240 (class 1259 OID 25277)
-- Name: team_my_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.team_my_user (
    id integer NOT NULL,
    my_user_id integer NOT NULL,
    team_id integer NOT NULL
);


ALTER TABLE public.team_my_user OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 25276)
-- Name: team_my_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.team_my_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.team_my_user_id_seq OWNER TO postgres;

--
-- TOC entry 4976 (class 0 OID 0)
-- Dependencies: 239
-- Name: team_my_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.team_my_user_id_seq OWNED BY public.team_my_user.id;


--
-- TOC entry 4711 (class 2604 OID 25089)
-- Name: direction id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.direction ALTER COLUMN id SET DEFAULT nextval('public.direction_id_seq'::regclass);


--
-- TOC entry 4709 (class 2604 OID 25071)
-- Name: form id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.form ALTER COLUMN id SET DEFAULT nextval('public.form_id_seq'::regclass);


--
-- TOC entry 4723 (class 2604 OID 25231)
-- Name: my_event id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_event ALTER COLUMN id SET DEFAULT nextval('public.my_event_id_seq'::regclass);


--
-- TOC entry 4716 (class 2604 OID 25132)
-- Name: my_group id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group ALTER COLUMN id SET DEFAULT nextval('public.my_group_id_seq'::regclass);


--
-- TOC entry 4725 (class 2604 OID 25247)
-- Name: my_group_my_event id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group_my_event ALTER COLUMN id SET DEFAULT nextval('public.my_group_my_event_id_seq'::regclass);


--
-- TOC entry 4728 (class 2604 OID 25297)
-- Name: my_group_requests id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group_requests ALTER COLUMN id SET DEFAULT nextval('public.my_group_requests_id_seq'::regclass);


--
-- TOC entry 4710 (class 2604 OID 25080)
-- Name: my_level id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_level ALTER COLUMN id SET DEFAULT nextval('public.my_level_id_seq'::regclass);


--
-- TOC entry 4708 (class 2604 OID 25060)
-- Name: my_role id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_role ALTER COLUMN id SET DEFAULT nextval('public.my_role_id_seq'::regclass);


--
-- TOC entry 4713 (class 2604 OID 25112)
-- Name: my_user id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_user ALTER COLUMN id SET DEFAULT nextval('public.my_user_id_seq'::regclass);


--
-- TOC entry 4717 (class 2604 OID 25176)
-- Name: news id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.news ALTER COLUMN id SET DEFAULT nextval('public.news_id_seq'::regclass);


--
-- TOC entry 4720 (class 2604 OID 25211)
-- Name: notifications id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications ALTER COLUMN id SET DEFAULT nextval('public.notifications_id_seq'::regclass);


--
-- TOC entry 4712 (class 2604 OID 25098)
-- Name: profile id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profile ALTER COLUMN id SET DEFAULT nextval('public.profile_id_seq'::regclass);


--
-- TOC entry 4726 (class 2604 OID 25264)
-- Name: team id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team ALTER COLUMN id SET DEFAULT nextval('public.team_id_seq'::regclass);


--
-- TOC entry 4727 (class 2604 OID 25280)
-- Name: team_my_user id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team_my_user ALTER COLUMN id SET DEFAULT nextval('public.team_my_user_id_seq'::regclass);


--
-- TOC entry 4936 (class 0 OID 25086)
-- Dependencies: 222
-- Data for Name: direction; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.direction (id, name, abbreviation) FROM stdin;
1	Информационные системы и технологии	ИФСТ
4	Информатика и вычислительная техника	ИВЧТ
6	Телевидение	ТЛВД
7	Реклама	РКЛМ
2	Прикладная информатика	ПИНФ
3	Программная инженерия	ПИНЖ
5	Дизайн	ДИЗН
8	Перевод и переводоведение	ПРП
\.


--
-- TOC entry 4932 (class 0 OID 25068)
-- Dependencies: 218
-- Data for Name: form; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.form (id, name, abbreviation) FROM stdin;
1	Очная	
2	Очно-заочная	оз
3	Заочная	з
4	Индивидуальный учебный план	ипу
\.


--
-- TOC entry 4948 (class 0 OID 25228)
-- Dependencies: 234
-- Data for Name: my_event; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.my_event (id, name, description, start_date, end_date, type, created_by, points, points_1st, points_2nd, points_3rd, points_participation, photo_url, max_participants, max_team_size, is_student_council_request, is_rejected, registration_link) FROM stdin;
22	Весенний хакатон ИнПИТ	Все желающие регистрируемся обязательно!	2025-05-22 13:00:00	2025-05-24 19:00:00	хакатон	7	\N	45	35	25	7	/uploads/images/a14c9bad-843c-45aa-a143-94da372cf493_inpit.jpg	70	5	f	\N	\N
4	Волонтерский выезд "Чистый берег"	Деканат совместно с эко-клубом "Зеленый вектор" объявляет о наборе волонтеров для участия в экологической акции по очистке берега реки [Название].\n\n Дата: 28 ноября 2025 г.\n Время сбора: 9:00 (площадь перед \n главным корпусом)\n Место: Парковая зона у реки [Название] (организованный выезд на автобусах)	2025-11-04 14:00:00	2025-11-04 20:00:00	волонтерство	7	15	\N	\N	\N	\N	/uploads/images/eabe0728-790a-4a96-a9f5-34334f238f85_graffiti-monrepo-01.jpg	20	\N	f	\N	\N
9	Акция «Добрые книги – детям»	Студсовет и волонтёрский центр института объявляют сбор детских книг для воспитанников местного приюта. Принимаются книги в хорошем состоянии (художественные, развивающие, учебники) для детей от 3 до 16 лет. Пункт сбора — холл 1-го корпуса (рядом со столовой). Акция продлится до 20 ноября. Также нужны волонтёры для сортировки книг и организации передачи. Записаться можно у координаторов – Анастасия (тел. 8-XXX-XXX-XX-XX) и Артём (группа ВК «Волонтёры ИнПИТ»).	2025-10-04 14:00:00	\N	волонтерство	5	15	\N	\N	\N	\N	/uploads/images/1f594935-95f4-42e0-ba5a-8870fdb9e402_depositphotos_5974998-stock-photo-stack-of-childrens-books.jpg	20	\N	t	f	\N
18	Экологический субботник в парке	Приглашаем всех желающих помочь в уборке и благоустройстве любимого городского парка! Субботник пройдёт 30 ноября с 10:00 до 14:00. Волонтёров ждут: уборка листвы, покраска лавочек, посадка деревьев и даже небольшой эко-квест с призами! Инвентарь выдадим на месте, но можно взять свои перчатки. Зарегистрироваться можно по ссылке или в кабинете студсовета до 25 ноября.	2025-05-09 15:59:00	2025-05-09 13:00:00	волонтерство	7	8	\N	\N	\N	\N	/uploads/images/9390a0e6-5d08-458a-9d0f-1ff51da071ce_ekskursiey-po-muzeyam-zavershilsya-volonterskiy-vyezd-v-gorodec-dlya-nizhegorodcev-fotografiya-7.jpg	10	\N	f	\N	\N
20	Помощь детскому дому	Организация творческих мастер-классов и игр для детей из детского дома "Солнышко"	2025-05-20 18:30:00	\N	волонтерство	5	15	\N	\N	\N	\N	/uploads/images/f1385a53-3916-4258-b7c3-17053b08f52f_fot6081.jpg	40	\N	t	\N	\N
24	КИНОВЕЧЕРㅤ	Приходите смотреть фильм Титаник	2025-05-22 19:00:00	\N	волонтерство	5	10	\N	\N	\N	\N	/uploads/images/eb0c59af-edb4-491a-a7f5-6c5a22a424da_event_image.jpg	\N	\N	t	f	\N
10	Хакатон от Сибинтек	Желающие участвовать записываемся!Вот ссылка: ...	2025-05-25 13:00:00	2025-05-28 16:00:00	хакатон_от_партнера	2	\N	50	40	30	10	/uploads/images/46fd1194-8d08-4018-a9b4-2c7d121ebfde_logotip_copy_49.jpg	\N	\N	f	\N	https://sibintek.ru/business-directions/
5	Хакатон от Неофлекс	Желающие участвовать записываемся!Вот ссылка: 	2025-05-17 20:31:00	2025-05-20 18:31:00	хакатон_от_партнера	7	\N	50	40	30	10	/uploads/images/3e41dac7-bf6a-4397-a659-34e667e81e50_cover-Neo-_2_.png	\N	\N	f	\N	https://www.neoflex.ru/project-list
3	Хакатон ИнПИТ	ЗАПИСЫВАЕМСЯ ВСЕ!	2025-05-18 13:00:00	2025-05-21 00:12:40.287886	хакатон	7	\N	50	40	30	10	/uploads/images/eb8d3bd3-b3d5-4239-b9fd-cba4282b36b5_f82fDz-gJBA.jpg	50	4	f	\N	\N
26	Хакатон Волна	Впервые проводим хакатон такого формата. Мест мало!	2025-05-20 12:00:00	2025-05-25 18:00:00	хакатон	7	\N	50	40	30	20	/uploads/images/a14c9bad-843c-45aa-a143-94da372cf493_inpit.jpg	20	2	f	\N	\N
25	Хакатон "Цифровой прорыв"	Участникам для решения требуется выбрать пять технологий реализации	2025-05-15 00:00:00	2025-05-17 00:00:00	хакатон	7	\N	50	40	20	10	/uploads/images/a14c9bad-843c-45aa-a143-94da372cf493_inpit.jpg	50	4	f	\N	\N
\.


--
-- TOC entry 4942 (class 0 OID 25129)
-- Dependencies: 228
-- Data for Name: my_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.my_group (id, profile_id, form_id, my_level_id, duration, number, leader_id, organizer_id, curator_id, description, start_date, direction_id) FROM stdin;
3	\N	2	1	5	3	\N	\N	\N	\N	2019-09-01	5
5	19	1	2	5	1	\N	\N	\N	\N	2002-09-01	1
6	19	3	1	5	1	\N	\N	\N	\N	2020-09-01	1
10	19	1	1	4	1	\N	\N	3	\N	2021-09-01	1
7	\N	3	3	3	2	\N	\N	\N	\N	2020-09-01	2
2	20	3	1	5	1	\N	\N	\N	\N	2022-09-01	1
9	17	2	1	5	2	\N	\N	8	ССЫЛКА НА ЧАТ	2022-09-01	5
8	18	4	1	5	1	\N	\N	8	Ссылка на чат	2023-09-01	5
12	19	1	1	4	2	4	28	32	Ссылка на чат группы: https://t.me/c/1617680305/2765	2022-09-01	1
11	20	1	1	4	1	25	22	32	Ссылка на чат группы: https://t.me/c/1617680305/2765	2022-09-01	1
4	\N	1	1	4	1	\N	\N	\N	\N	2021-09-01	4
1	19	1	1	4	2	\N	\N	3	\N	2021-09-01	1
\.


--
-- TOC entry 4950 (class 0 OID 25244)
-- Dependencies: 236
-- Data for Name: my_group_my_event; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.my_group_my_event (id, my_group_id, my_event_id) FROM stdin;
\.


--
-- TOC entry 4956 (class 0 OID 25294)
-- Dependencies: 242
-- Data for Name: my_group_requests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.my_group_requests (id, my_user_id, my_group_id) FROM stdin;
11	20	11
13	31	12
\.


--
-- TOC entry 4934 (class 0 OID 25077)
-- Dependencies: 220
-- Data for Name: my_level; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.my_level (id, name, abbreviation) FROM stdin;
1	Бакалавриат	б
2	Специалитет	с
3	Магистратура	м
\.


--
-- TOC entry 4930 (class 0 OID 25057)
-- Dependencies: 216
-- Data for Name: my_role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.my_role (id, name) FROM stdin;
1	Администратор
2	Администрация
3	Куратор
4	Студент
5	Студсовет
\.


--
-- TOC entry 4940 (class 0 OID 25109)
-- Dependencies: 226
-- Data for Name: my_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.my_user (id, first_name, last_name, patronymic, email, login, password, phone, photo_url, points, is_blocked, my_role_id, my_group_id) FROM stdin;
5	Игорь	Смирнов	Юрьевич	i.smirnov@example.com	i.smirnov	MzLo48@NkP2l	\N	\N	0	f	5	\N
10	Наталья	Мельникова	Дмитриевна	n.melnikova@example.com	n.melnikova	NzLKmO81@Pq29	\N	\N	0	f	5	\N
1	Алексей	Иванов	Сергеевич	a.ivanov@yandex.ru	a.ivanov	Ghj84!KdLpz2	\N	\N	0	f	1	\N
21	Роман	Лебедев	Ильич	r.lebedev@example.com	r.lebedev	Pwd3@group8	\N	\N	0	f	4	12
7	Татьяна	Петрова	Юрьевна	petrovatu@mail.ru	petrovatu	KmLz81@NpO2q	\N	\N	0	f	2	\N
27	Владислав	Никитин	Георгиевич	v.nikitin@example.com	v.nikitin	Pwd4@group9	\N	\N	10	f	4	11
9	Артем	Гаврилов	Максимович	a.gavrilov@example.com	a.gavrilov	XzLKm93@NpO1q	\N	\N	40	f	4	12
19	Иван	Ковалев	Алексеевич	i.kovalev@example.com	i.kovalev	Pwd1@group8	\N	\N	40	f	4	11
6	Анна	Федорова	Сергеевна	annaaaa@ya.ru	a.fedorovaa	LzNp9!BmKx20	\N	\N	0	f	1	\N
28	Наталья	Филиппова	Андреевна	n.filippova@example.com	n.filippova	Pwd5@group9	\N	\N	0	f	4	12
2	Мария	Петрова	Александровна	m.petrova@example.com	m.petrova	YdL0k!XmNp92	\N	\N	0	f	2	\N
4	Елена	Козлова	Васильевна	e.kozlova@example.com	e.kozlova	BzKp12!LxN9q	\N	\N	50	f	4	12
3	Дмитрий	Пиминов	Алексеевич	d.piminov@yandex.ru	d.piminov	Plz93@VkLm2o	\N	\N	0	f	3	\N
20	Светлана	Миронова	Викторовна	s.mironova@example.com	s.mironova	Pwd2@group8	\N	\N	0	f	4	\N
32	Екатерина	Кулакова	Михайловна	kulakovaem@yandex.ru	kulakovaem	rtGKvd1lCYf3	\N	/uploads/images/5c8a0517-b33a-44e2-b92d-8fd209c65717_Kulakova-EM_450.jpg	0	f	3	\N
23	Денис	Сорокин	Аркадьевич	d.sorokin@example.com	d.sorokin	Pwd5@group8	\N	\N	50	f	4	12
26	Евгения	Семенова	Юрьевна	e.semenova@example.com	e.semenova	Pwd3@group9	\N	\N	40	f	4	11
33	Мария	Кирилушкина	Владимировна	kiril.mv@mail.ru	login	password	88005553535	\N	0	f	4	1
31	Алина	Федорова	Анатольевна	alfasar1@yandex.ru	alinafedorova	5_943NE0ecOI	\N	\N	0	f	4	\N
18	Алексей	Алексеев	Алексеевич	emma@yandex.ru	mv.emma	lM92MCn2ZePG	\N	\N	0	f	5	\N
8	Ольга	Васильева-Картошкина	Игоревна	o.vasileva@example.com	o.vasileva	PzLNk12!BmX9o	\N	/uploads/images/cf0b0358-a3ae-43a8-b147-8fe7c72c6837_pashtet.jpg	0	f	3	\N
25	Михаил	Зайцев	Валерьевич	m.zaytsev@example.com	m.zaytsev	Pwd2@group9	\N	/uploads/images/4078f42c-de31-4277-9c5b-909109d6ea05_234069da73de11ee898a2aa0df1cd6e5_upscaled.jpg	28	f	4	11
24	Татьяна	Егорова	Максимовна	t.egorova@example.com	t.egorova	Pwd1@group9	\N	\N	0	f	4	12
22	Оксана	Григорьева	Петровна	o.grigorieva@example.com	o.grigorieva	Pwd4@group8	\N	\N	8	f	4	11
\.


--
-- TOC entry 4944 (class 0 OID 25173)
-- Dependencies: 230
-- Data for Name: news; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.news (id, title, content, author_id, my_group_id, created_at, photo_url, is_student_council_request, is_rejected) FROM stdin;
32	Сбор предложений по улучшению студенческой жизни	Твой голос важен! Студсовет запускает сбор идей по улучшению жизни в институте: от организации мероприятий до изменений в расписании или работе столовой. Оставляй свои предложения до 15 декабря в специальном ящике возле деканата или в обсуждениях группы студсовета. Лучшие идеи будут рассмотрены на встрече с администрацией вуза. Давай вместе сделаем наш институт лучше!!	5	\N	2025-05-02 18:15:19.982164	/uploads/images/fd27721d-9e97-4b42-beff-38fb078e5ee4_3-70.jpg	t	f
10	Мероприятие от куратора	Сегодня будет собрание старост и профоргов по расписанию	32	11	2025-04-19 21:45:08.726002	/uploads/images/d396f520-93b2-495a-8ea0-5bae6c3efb86_big_82f63f89885428f52dc3e2574e718afa21012cf41e4fb1eecb24523706f1f7e.jpg	f	\N
34	Набор студентов для участия в международной научной конференции	Деканат объявляет о начале отбора студентов для участия в Международной научной конференции «Инновации в современном образовании», которая пройдет в феврале 2025 года. Заявки принимаются до 20 января. Участники получат возможность представить свои исследовательские работы и обменяться опытом с коллегами из других вузов. Подробности и условия участия — в учебном отделе деканата.	7	\N	2025-05-02 21:07:19.844069	/uploads/images/618efdc7-390a-42d0-a7d1-6ff89298170e_11.jpg	f	\N
5	Весенний субботник	Собираемся на уборку парка во вторник в 11:00 на ул. Рахова д.32	32	12	2025-04-17 23:46:31.188501	/uploads/images/6c427025-22b2-42b5-a7df-06b6b3c4e7aa_g75rnzxfitw2e4u.jpg	f	\N
29	уборка завтра всем быть в 13.00	Уборка территории, завтра всем быть в 13.00 в холле 1 корпуса.	32	11	2025-05-01 16:32:45.780668	/uploads/images/1b4f3e5d-ff4b-463a-b7de-7e8e24485d75_sgtu1.jpg	f	\N
6	Объявление для группы	Занятие по математике переносится на пятницу в 13:40.	32	12	2025-04-17 23:48:59.343046	/uploads/images/f4db043f-016a-4c77-b0b9-293a65648256_images.jpg	f	\N
31	Изменение графика сдачи сессии для студентов 1-3 курсов	Деканат института сообщает, что в связи с проведением ремонтных работ в главном корпусе график сдачи зимней сессии для студентов 1-3 курсов будет скорректирован. Новое расписание экзаменов будет опубликовано на официальном сайте института и на информационных стендах не позднее 15 декабря. Студентам рекомендуется уточнять даты экзаменов у своих кураторов.	7	\N	2025-05-01 23:02:46.257928	/uploads/images/03cd3dd5-fc83-4e20-9db7-5a49aabcd6e9_63bfc031e335a130682247.jpg	f	\N
42	Просмотр фильма	Завтра в общежитии №3 состоится просмотр фильма "А зори здесь тихие"	5	\N	2025-05-17 21:57:16.039041	/uploads/images/314ba62d-ab14-44b1-92fc-ff55e014b454_f6275046cd53c49fababd5116ca5c42a.jpg	t	t
46	Ура! Очередная победа!	Мы выиграли хакатон	5	\N	2025-05-21 14:02:51.179652	/uploads/images/efefe817-f26f-435c-9de2-f01a3fcc78af_news_image.jpg	t	f
\.


--
-- TOC entry 4946 (class 0 OID 25208)
-- Dependencies: 232
-- Data for Name: notifications; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notifications (id, type, text, created_at, my_group_id, reciever_id, is_read) FROM stdin;
1	система	В группе б-ИВЧТ-41 новый куратор	2025-05-01 22:16:21.920611	\N	\N	f
2	новость	Опубликована новость: Новость от 	2025-05-01 23:02:46.284102	\N	\N	f
3	система	В группе б1-ИФСТ-42 новый куратор	2025-05-01 23:05:26.576271	\N	\N	f
4	новость	Опубликована новость: Подробное описание от студсовета	2025-05-02 18:15:20.130436	\N	\N	f
5	новость	Опубликована новость: Подробное описание от студсовета	2025-05-02 18:31:12.601304	\N	\N	f
7	новость	Опубликована новость: 4 декабря 2024 у Ермакова Александра Вадимовича родилась чудесная девочка Алиса! 51 см, 3200 г! Поздравляем всем ИнПИТом!	2025-05-02 23:37:36.297327	\N	\N	f
13	новость	Опубликована новость: c	2025-05-20 15:18:29.577061	\N	\N	f
14	новость	Опубликована новость: gngn	2025-05-20 15:25:16.433531	\N	\N	f
15	новость	Опубликована новость: йййй	2025-05-21 13:54:06.028008	\N	\N	f
16	новость	Опубликована новость: Мы выиграли	2025-05-21 14:02:51.189858	\N	\N	f
\.


--
-- TOC entry 4938 (class 0 OID 25095)
-- Dependencies: 224
-- Data for Name: profile; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.profile (id, name, direction_id, number) FROM stdin;
17	Графический дизайн	5	1
18	Промышленный дизайн	5	2
19	Информационные системы и технологии	1	1
20	Информационные системы и технологии в медиаиндустрии	1	2
\.


--
-- TOC entry 4952 (class 0 OID 25261)
-- Dependencies: 238
-- Data for Name: team; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.team (id, name, my_event_id, place, diploma, is_confirmed) FROM stdin;
5	Зайцев Михаил Валерьевич	9	\N	\N	\N
13	Сорокин Денис Аркадьевич	3	1	/uploads/images/1c78a43e-675c-4027-a784-943bf997ee65_cover-Neo-_2_.png	\N
2	Амогусы	3	2	/uploads/images/2a05c9d8-4dff-474c-9240-1d61738f6a6f_5c803262d57a1-700x471.jpg	\N
8	Зайцев Михаил Валерьевич	18	\N	\N	\N
9	Григорьева Оксана Петрова	18	\N	\N	\N
7	Козлова Елена Васильевна	5	1	/uploads/images/ab4a13b0-ba33-4207-a8e6-b31b112f5d7a_Федорова Алина.pdf	t
3	Никитин Владислав Георгиевич	5	\N	/uploads/images/ab4a13b0-ba33-4207-a8e6-b31b112f5d7a_Федорова Алина.pdf	t
6	боссы художки	5	2	/uploads/images/ab4a13b0-ba33-4207-a8e6-b31b112f5d7a_Федорова Алина.pdf	t
14	боссы KFC	10	1	/uploads/images/ab4a13b0-ba33-4207-a8e6-b31b112f5d7a_Федорова Алина.pdf	\N
15	найк про	10	2	/uploads/images/ab4a13b0-ba33-4207-a8e6-b31b112f5d7a_Федорова Алина.pdf	\N
16	Зайцев Михаил Валерьевич	10	\N	/uploads/images/ab4a13b0-ba33-4207-a8e6-b31b112f5d7a_Федорова Алина.pdf	\N
17	Палитры	22	\N	\N	\N
18	Сладкие плюшки	22	\N	\N	\N
20	биба и боба	22	\N	\N	t
21	Мария	4	\N	\N	t
19	дыня	10	\N	\N	f
25	Бобры	25	3	/uploads/images/ca5c1168-2c62-4dd6-99aa-e4d998f7bf46_diploma_1747827747889.jpg	t
27	Михаил	4	\N	\N	\N
\.


--
-- TOC entry 4954 (class 0 OID 25277)
-- Dependencies: 240
-- Data for Name: team_my_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.team_my_user (id, my_user_id, team_id) FROM stdin;
2	25	2
3	26	2
4	27	3
6	25	5
8	9	6
9	19	6
10	4	7
11	25	8
12	22	9
18	23	13
19	31	14
20	24	14
21	22	14
22	20	15
23	23	15
24	25	16
25	21	17
26	27	17
27	31	18
29	33	19
30	33	20
31	33	21
35	25	25
37	25	27
\.


--
-- TOC entry 4977 (class 0 OID 0)
-- Dependencies: 221
-- Name: direction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.direction_id_seq', 8, true);


--
-- TOC entry 4978 (class 0 OID 0)
-- Dependencies: 217
-- Name: form_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.form_id_seq', 4, true);


--
-- TOC entry 4979 (class 0 OID 0)
-- Dependencies: 233
-- Name: my_event_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.my_event_id_seq', 26, true);


--
-- TOC entry 4980 (class 0 OID 0)
-- Dependencies: 227
-- Name: my_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.my_group_id_seq', 12, true);


--
-- TOC entry 4981 (class 0 OID 0)
-- Dependencies: 235
-- Name: my_group_my_event_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.my_group_my_event_id_seq', 1, false);


--
-- TOC entry 4982 (class 0 OID 0)
-- Dependencies: 241
-- Name: my_group_requests_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.my_group_requests_id_seq', 13, true);


--
-- TOC entry 4983 (class 0 OID 0)
-- Dependencies: 219
-- Name: my_level_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.my_level_id_seq', 3, true);


--
-- TOC entry 4984 (class 0 OID 0)
-- Dependencies: 215
-- Name: my_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.my_role_id_seq', 5, true);


--
-- TOC entry 4985 (class 0 OID 0)
-- Dependencies: 225
-- Name: my_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.my_user_id_seq', 33, true);


--
-- TOC entry 4986 (class 0 OID 0)
-- Dependencies: 229
-- Name: news_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.news_id_seq', 46, true);


--
-- TOC entry 4987 (class 0 OID 0)
-- Dependencies: 231
-- Name: notifications_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.notifications_id_seq', 16, true);


--
-- TOC entry 4988 (class 0 OID 0)
-- Dependencies: 223
-- Name: profile_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.profile_id_seq', 20, true);


--
-- TOC entry 4989 (class 0 OID 0)
-- Dependencies: 237
-- Name: team_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.team_id_seq', 28, true);


--
-- TOC entry 4990 (class 0 OID 0)
-- Dependencies: 239
-- Name: team_my_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.team_my_user_id_seq', 38, true);


--
-- TOC entry 4739 (class 2606 OID 25093)
-- Name: direction direction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.direction
    ADD CONSTRAINT direction_pkey PRIMARY KEY (id);


--
-- TOC entry 4735 (class 2606 OID 25075)
-- Name: form form_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.form
    ADD CONSTRAINT form_pkey PRIMARY KEY (id);


--
-- TOC entry 4755 (class 2606 OID 25237)
-- Name: my_event my_event_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_event
    ADD CONSTRAINT my_event_pkey PRIMARY KEY (id);


--
-- TOC entry 4757 (class 2606 OID 25249)
-- Name: my_group_my_event my_group_my_event_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group_my_event
    ADD CONSTRAINT my_group_my_event_pkey PRIMARY KEY (id);


--
-- TOC entry 4749 (class 2606 OID 25136)
-- Name: my_group my_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group
    ADD CONSTRAINT my_group_pkey PRIMARY KEY (id);


--
-- TOC entry 4763 (class 2606 OID 25299)
-- Name: my_group_requests my_group_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group_requests
    ADD CONSTRAINT my_group_requests_pkey PRIMARY KEY (id);


--
-- TOC entry 4737 (class 2606 OID 25084)
-- Name: my_level my_level_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_level
    ADD CONSTRAINT my_level_pkey PRIMARY KEY (id);


--
-- TOC entry 4731 (class 2606 OID 25066)
-- Name: my_role my_role_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_role
    ADD CONSTRAINT my_role_name_key UNIQUE (name);


--
-- TOC entry 4733 (class 2606 OID 25064)
-- Name: my_role my_role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_role
    ADD CONSTRAINT my_role_pkey PRIMARY KEY (id);


--
-- TOC entry 4743 (class 2606 OID 25120)
-- Name: my_user my_user_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_user
    ADD CONSTRAINT my_user_email_key UNIQUE (email);


--
-- TOC entry 4745 (class 2606 OID 25122)
-- Name: my_user my_user_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_user
    ADD CONSTRAINT my_user_login_key UNIQUE (login);


--
-- TOC entry 4747 (class 2606 OID 25118)
-- Name: my_user my_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_user
    ADD CONSTRAINT my_user_pkey PRIMARY KEY (id);


--
-- TOC entry 4751 (class 2606 OID 25183)
-- Name: news news_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.news
    ADD CONSTRAINT news_pkey PRIMARY KEY (id);


--
-- TOC entry 4753 (class 2606 OID 25216)
-- Name: notifications notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- TOC entry 4741 (class 2606 OID 25102)
-- Name: profile profile_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT profile_pkey PRIMARY KEY (id);


--
-- TOC entry 4761 (class 2606 OID 25282)
-- Name: team_my_user team_my_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team_my_user
    ADD CONSTRAINT team_my_user_pkey PRIMARY KEY (id);


--
-- TOC entry 4759 (class 2606 OID 25269)
-- Name: team team_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT team_pkey PRIMARY KEY (id);


--
-- TOC entry 4765 (class 2606 OID 25167)
-- Name: my_user fk_my_group; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_user
    ADD CONSTRAINT fk_my_group FOREIGN KEY (my_group_id) REFERENCES public.my_group(id);


--
-- TOC entry 4778 (class 2606 OID 25238)
-- Name: my_event my_event_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_event
    ADD CONSTRAINT my_event_created_by_fkey FOREIGN KEY (created_by) REFERENCES public.my_user(id);


--
-- TOC entry 4767 (class 2606 OID 25162)
-- Name: my_group my_group_curator_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group
    ADD CONSTRAINT my_group_curator_id_fkey FOREIGN KEY (curator_id) REFERENCES public.my_user(id);


--
-- TOC entry 4768 (class 2606 OID 25311)
-- Name: my_group my_group_direction_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group
    ADD CONSTRAINT my_group_direction_id_fkey FOREIGN KEY (direction_id) REFERENCES public.direction(id);


--
-- TOC entry 4769 (class 2606 OID 25142)
-- Name: my_group my_group_form_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group
    ADD CONSTRAINT my_group_form_id_fkey FOREIGN KEY (form_id) REFERENCES public.form(id);


--
-- TOC entry 4770 (class 2606 OID 25152)
-- Name: my_group my_group_leader_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group
    ADD CONSTRAINT my_group_leader_id_fkey FOREIGN KEY (leader_id) REFERENCES public.my_user(id);


--
-- TOC entry 4779 (class 2606 OID 25255)
-- Name: my_group_my_event my_group_my_event_my_event_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group_my_event
    ADD CONSTRAINT my_group_my_event_my_event_id_fkey FOREIGN KEY (my_event_id) REFERENCES public.my_event(id);


--
-- TOC entry 4780 (class 2606 OID 25250)
-- Name: my_group_my_event my_group_my_event_my_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group_my_event
    ADD CONSTRAINT my_group_my_event_my_group_id_fkey FOREIGN KEY (my_group_id) REFERENCES public.my_group(id);


--
-- TOC entry 4771 (class 2606 OID 25147)
-- Name: my_group my_group_my_level_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group
    ADD CONSTRAINT my_group_my_level_id_fkey FOREIGN KEY (my_level_id) REFERENCES public.my_level(id);


--
-- TOC entry 4772 (class 2606 OID 25157)
-- Name: my_group my_group_organizer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group
    ADD CONSTRAINT my_group_organizer_id_fkey FOREIGN KEY (organizer_id) REFERENCES public.my_user(id);


--
-- TOC entry 4773 (class 2606 OID 25137)
-- Name: my_group my_group_profile_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group
    ADD CONSTRAINT my_group_profile_id_fkey FOREIGN KEY (profile_id) REFERENCES public.profile(id);


--
-- TOC entry 4784 (class 2606 OID 25305)
-- Name: my_group_requests my_group_requests_my_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group_requests
    ADD CONSTRAINT my_group_requests_my_group_id_fkey FOREIGN KEY (my_group_id) REFERENCES public.my_group(id);


--
-- TOC entry 4785 (class 2606 OID 25300)
-- Name: my_group_requests my_group_requests_my_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_group_requests
    ADD CONSTRAINT my_group_requests_my_user_id_fkey FOREIGN KEY (my_user_id) REFERENCES public.my_user(id);


--
-- TOC entry 4766 (class 2606 OID 25123)
-- Name: my_user my_user_my_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.my_user
    ADD CONSTRAINT my_user_my_role_id_fkey FOREIGN KEY (my_role_id) REFERENCES public.my_role(id);


--
-- TOC entry 4774 (class 2606 OID 25184)
-- Name: news news_author_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.news
    ADD CONSTRAINT news_author_id_fkey FOREIGN KEY (author_id) REFERENCES public.my_user(id);


--
-- TOC entry 4775 (class 2606 OID 25189)
-- Name: news news_my_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.news
    ADD CONSTRAINT news_my_group_id_fkey FOREIGN KEY (my_group_id) REFERENCES public.my_group(id);


--
-- TOC entry 4776 (class 2606 OID 25217)
-- Name: notifications notifications_my_group_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_my_group_id_fkey FOREIGN KEY (my_group_id) REFERENCES public.my_group(id);


--
-- TOC entry 4777 (class 2606 OID 25222)
-- Name: notifications notifications_reciever_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_reciever_id_fkey FOREIGN KEY (reciever_id) REFERENCES public.my_user(id);


--
-- TOC entry 4764 (class 2606 OID 25103)
-- Name: profile profile_direction_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT profile_direction_id_fkey FOREIGN KEY (direction_id) REFERENCES public.direction(id);


--
-- TOC entry 4781 (class 2606 OID 25270)
-- Name: team team_my_event_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT team_my_event_id_fkey FOREIGN KEY (my_event_id) REFERENCES public.my_event(id);


--
-- TOC entry 4782 (class 2606 OID 25283)
-- Name: team_my_user team_my_user_my_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team_my_user
    ADD CONSTRAINT team_my_user_my_user_id_fkey FOREIGN KEY (my_user_id) REFERENCES public.my_user(id);


--
-- TOC entry 4783 (class 2606 OID 25288)
-- Name: team_my_user team_my_user_team_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team_my_user
    ADD CONSTRAINT team_my_user_team_id_fkey FOREIGN KEY (team_id) REFERENCES public.team(id);


-- Completed on 2025-06-05 12:04:31

--
-- PostgreSQL database dump complete
--

