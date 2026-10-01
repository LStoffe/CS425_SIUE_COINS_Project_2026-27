SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
-- SET transaction_timeout = 0;
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
-- Name: activity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.activity (
    activity_type_id smallint NOT NULL,
    gameid integer NOT NULL,
    jobid integer NOT NULL,
    periodid integer NOT NULL,
    lus_total integer,
    lus_completed integer,
    current_labor_cost double precision,
    current_material_cost double precision,
    current_equipment_cost double precision,
    current_subcontractor_cost double precision,
    is_active boolean,
    overtime_days integer,
    lus_completed_this_period integer,
    accumulated_costs double precision,
    current_method_used smallint,
    amount_billed bigint
);



--
-- Name: activity_parameters; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.activity_parameters (
    activity_type_id smallint NOT NULL,
    job_type_id smallint NOT NULL,
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    finish_to_finish_pct smallint,
    finish_to_finish_activity smallint,
    start_to_start_pct smallint,
    start_to_start_activity smallint
);



--
-- Name: admin; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.admin (
    adminid integer NOT NULL,
    username character varying(20),
    password character varying(255),
    is_head_admin boolean,
    email character varying(80)
);



--
-- Name: appraisal_metrics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.appraisal_metrics (
    companyid integer NOT NULL,
    periodid integer NOT NULL,
    financial_liquidity smallint,
    financial_success smallint,
    bid_responsibility smallint,
    pace smallint,
    ethics smallint,
    name_recognition smallint,
    apartments smallint,
    schools smallint,
    offices smallint,
    hospitals smallint,
    industrial smallint,
    highways smallint,
    bridges smallint,
    sitedevelopment smallint,
    massexcavation smallint,
    undergroundutilities smallint
);



--
-- Name: appraisal_metrics_updates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.appraisal_metrics_updates (
    companyid integer NOT NULL,
    financial_liquidity smallint,
    financial_success smallint,
    bid_responsibility smallint,
    pace smallint,
    ethics smallint,
    name_recognition smallint,
    apartments smallint,
    schools smallint,
    offices smallint,
    hospitals smallint,
    industrial smallint,
    highways smallint,
    bridges smallint,
    sitedevelopment smallint,
    massexcavation smallint,
    undergroundutilities smallint
);



--
-- Name: available_equipment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.available_equipment (
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    equipment_title character varying(80) NOT NULL,
    equipment_cost_per_period integer NOT NULL,
    equipment_deploy_cost integer NOT NULL,
    equipment_decommission_cost integer NOT NULL,
    equipment_description character varying NOT NULL,
    equipment_prcnt_raise smallint NOT NULL,
    equipment_base_points integer NOT NULL,
    equipment_id integer NOT NULL
);



--
-- Name: available_personnel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.available_personnel (
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    category integer NOT NULL,
    employee_title character varying(80) NOT NULL,
    employee_cost_per_period integer NOT NULL,
    employee_hiring_cost integer NOT NULL,
    employee_firing_cost integer NOT NULL,
    employee_description character varying NOT NULL,
    employee_prcnt_raise smallint NOT NULL,
    employee_base_points integer NOT NULL
);



--
-- Name: balance_sheet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.balance_sheet (
    gameid integer NOT NULL,
    companyid integer NOT NULL,
    periodid integer NOT NULL,
    cash_on_hand double precision,
    current_accounts_receivable double precision,
    retention_receivable double precision,
    construction_equipment double precision,
    autos double precision,
    office_equipment double precision,
    acc_depreciation double precision,
    land double precision,
    current_accounts_payable double precision,
    retention_payable double precision,
    income_taxes_payable double precision,
    loan_balances double precision,
    equity double precision
);



--
-- Name: bid; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bid (
    jobid integer NOT NULL,
    companyid integer NOT NULL,
    gameid integer NOT NULL,
    bid_amount double precision,
    bid_amount_up double precision,
    unitprice1 double precision,
    unitprice2 double precision,
    unitprice3 double precision,
    unitprice4 double precision,
    unitprice5 double precision,
    unitprice6 double precision,
    unitprice7 double precision,
    unitprice8 double precision,
    unitprice9 double precision,
    outcome_code smallint
);



--
-- Name: bid_method; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bid_method (
    jobid integer NOT NULL,
    companyid integer NOT NULL,
    gameid integer NOT NULL,
    bid_activity smallint NOT NULL,
    bid_method smallint
);



--
-- Name: big_adminid_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.big_adminid_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: big_bidid_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.big_bidid_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: big_cccid_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.big_cccid_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: big_companyid_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.big_companyid_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: big_customerid_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.big_customerid_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: big_gameid_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.big_gameid_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: big_loanid_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.big_loanid_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: billing_breakdown; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.billing_breakdown (
    gameid integer NOT NULL,
    companyid integer NOT NULL,
    periodid integer NOT NULL,
    jobid integer NOT NULL,
    activityid integer NOT NULL,
    amount bigint
);



--
-- Name: billings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.billings (
    gameid integer NOT NULL,
    companyid integer NOT NULL,
    jobid integer NOT NULL,
    periodid integer NOT NULL,
    amount_billed double precision,
    old_amount_billed double precision,
    amount_paid double precision,
    rejected boolean
);



--
-- Name: cash_flow; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cash_flow (
    gameid integer NOT NULL,
    companyid integer NOT NULL,
    periodid integer NOT NULL,
    amount double precision,
    inflow boolean,
    source character varying(20)
);



--
-- Name: ccc_job_type_preferences; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ccc_job_type_preferences (
    cccid integer NOT NULL,
    job_type_id integer NOT NULL,
    preference smallint
);



--
-- Name: company; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company (
    companyid integer NOT NULL,
    gameid integer NOT NULL,
    title character varying(100),
    username character varying(20),
    password character varying(255),
    license integer,
    cash_on_hand double precision,
    wip_limit double precision,
    wip_limit_percent integer,
    per_job_limit double precision,
    per_job_wip_percent integer,
    wip integer,
    missionstatement text,
    corevalues text,
    companyurl text
);



--
-- Name: company_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_members (
    companyid integer,
    gameid integer,
    firstname character varying(20),
    lastname character varying(20),
    email character varying(80)
);



--
-- Name: company_personnel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_personnel (
    gameid integer NOT NULL,
    companyid integer NOT NULL,
    periodid integer NOT NULL,
    employee_title character varying(80) NOT NULL,
    employee_type_count smallint NOT NULL,
    employee_type_adds smallint NOT NULL,
    employee_type_loss smallint NOT NULL
);



--
-- Name: component; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.component (
    gameid integer NOT NULL,
    estimating smallint NOT NULL,
    autobilling smallint DEFAULT 0 NOT NULL
);



--
-- Name: computer_controlled_contractors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.computer_controlled_contractors (
    cccid integer NOT NULL,
    gameid integer NOT NULL,
    fitness smallint,
    name character varying(80),
    current boolean
);



--
-- Name: contract_reports; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.contract_reports (
    gameid integer NOT NULL,
    companyid integer NOT NULL,
    periodid integer NOT NULL,
    jobid integer NOT NULL,
    previous_cost double precision,
    current_cost double precision,
    billed_to_date double precision,
    est_cost_to_complete double precision,
    revenue_recorded double precision,
    big_computes_est_cost boolean
);



--
-- Name: current_methods; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.current_methods (
    gameid integer NOT NULL,
    jobid integer NOT NULL,
    activity_type_id smallint NOT NULL,
    method_id smallint
);



--
-- Name: customer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer (
    customerid integer DEFAULT 0 NOT NULL,
    customer_name character varying(80),
    fin_liq_factor smallint,
    fin_liq_level smallint,
    fin_suc_factor smallint,
    fin_suc_level smallint,
    responsibility_factor smallint,
    responsibility_level smallint,
    pace_factor smallint,
    pace_level smallint,
    ethics_factor smallint,
    ethics_level smallint,
    name_rec_factor smallint,
    name_rec_level smallint,
    job_type_factor smallint,
    job_type_level smallint
);



--
-- Name: customer_company_opinions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer_company_opinions (
    customerid integer NOT NULL,
    companyid integer NOT NULL,
    opinion smallint NOT NULL
);



--
-- Name: dates_update_policy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dates_update_policy (
    gameid integer NOT NULL,
    date date NOT NULL,
    update_time time without time zone NOT NULL,
    date_hash bigint NOT NULL,
    complete boolean NOT NULL
);



--
-- Name: days_update_policy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.days_update_policy (
    gameid integer NOT NULL,
    day smallint NOT NULL,
    update_time time without time zone NOT NULL
);



--
-- Name: equip_network_dep; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.equip_network_dep (
    activity_type_id smallint NOT NULL,
    job_type_id smallint NOT NULL,
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    equip_type integer NOT NULL,
    quantity_of_equip integer NOT NULL,
    percent_time integer NOT NULL
);



--
-- Name: equipment_to_period_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.equipment_to_period_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: estimates_available; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.estimates_available (
    gameid integer NOT NULL,
    companyid integer NOT NULL,
    jobid integer NOT NULL
);



--
-- Name: estimates_per_period; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.estimates_per_period (
    gameid integer NOT NULL,
    estimates smallint NOT NULL
);



--
-- Name: financial_report; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.financial_report (
    gameid integer NOT NULL,
    companyid integer NOT NULL,
    periodid integer NOT NULL,
    interest_paid double precision,
    bidding_expenses double precision,
    consulting_expenses double precision,
    office_overhead double precision,
    legal_fees double precision,
    settlement double precision,
    previous_cash_on_hand double precision,
    loan_principle_payments double precision
);



--
-- Name: game; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game (
    gameid integer NOT NULL,
    gamename character varying(100),
    adminid integer,
    current_period integer,
    start_year date,
    is_active boolean,
    current_job_number integer,
    gametype integer
);



--
-- Name: game_params_ccc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_ccc (
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    min_number_cccs smallint,
    max_number_cccs smallint,
    min_ccc_bids_per_job smallint,
    max_ccc_bids_per_job smallint,
    ccc_turnover smallint,
    upper_profit_limit smallint,
    lower_profit_limit smallint
);



--
-- Name: game_params_job_size; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_job_size (
    gameid integer NOT NULL,
    job_type_id smallint NOT NULL,
    adminid integer NOT NULL,
    job_size_mean integer,
    job_size_stddev integer,
    job_size_min integer,
    job_size_max integer
);



--
-- Name: game_params_jobs_per_period; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_jobs_per_period (
    gameid integer NOT NULL,
    periodid integer NOT NULL,
    adminid integer NOT NULL,
    number_jobs_mean integer,
    number_jobs_stddev integer,
    number_jobs_min integer,
    number_jobs_max integer,
    number_hc_jobs_min integer,
    number_hc_jobs_max integer
);



--
-- Name: game_params_labor_avail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_labor_avail (
    gameid integer NOT NULL,
    periodid smallint NOT NULL,
    adminid integer NOT NULL,
    labor_avail_mean integer,
    labor_avail_stddev integer,
    labor_avail_min integer,
    labor_avail_max integer
);



--
-- Name: game_params_liquidated_damages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_liquidated_damages (
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    mean_dc_percent real,
    stddev_dc_percent real,
    min_dc_percent real,
    max_dc_percent real,
    percent_jobs smallint,
    faster_jobs_increase boolean,
    larger_jobs_increase boolean
);



--
-- Name: game_params_material_cost_idx; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_material_cost_idx (
    gameid integer NOT NULL,
    periodid smallint NOT NULL,
    adminid integer NOT NULL,
    material_cost_index_mean integer,
    material_cost_index_stddev integer,
    material_cost_index_min integer,
    material_cost_index_max integer
);



--
-- Name: game_params_misc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_misc (
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    workdays_per_month integer,
    standard_hrs_per_day integer,
    overtime_hrs_per_day integer,
    bid_expense_percent smallint,
    loan_interest_per_year smallint,
    max_jobs_per_team smallint,
    max_loans_per_team smallint,
    max_loan_amount integer,
    loan_repayment_periods smallint,
    overtime_expense_factor real,
    max_overbilling_percent smallint,
    income_tax_percent smallint,
    depreciation_percent smallint,
    overbilling_eff_eth_lim smallint,
    bond_reeval_frequency smallint
);



--
-- Name: game_params_negotiation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_negotiation (
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    word_of_mouth_rate_min smallint,
    word_of_mouth_rate_max smallint,
    word_of_mouth_insty_min smallint,
    word_of_mouth_insty_max smallint,
    pct_closed_bids_to_neg smallint,
    mean_pct_profit smallint,
    std_dev_pct_profit real,
    min_pct_profit smallint,
    max_pct_profit smallint,
    lkhd_customer_breaks_neg smallint,
    est_eff_ethics_limit smallint,
    customer_haggling_mean smallint,
    customer_haggling_std_dev real,
    customer_haggling_min smallint,
    customer_haggling_max smallint
);



--
-- Name: game_params_overhead; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_overhead (
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    field_overhead_fixed integer,
    field_overhead_variable integer,
    office_overhead_fixed integer
);



--
-- Name: game_params_percent_takeoff; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_percent_takeoff (
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    job_type_id smallint NOT NULL,
    activity_type_id smallint NOT NULL,
    percent_takeoff integer
);



--
-- Name: game_params_rainfall; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_rainfall (
    gameid integer NOT NULL,
    periodid smallint NOT NULL,
    adminid integer NOT NULL,
    rainfall_mean integer,
    rainfall_stddev integer,
    rainfall_min integer,
    rainfall_max integer
);



--
-- Name: game_params_report_costs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_report_costs (
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    complete_list_report integer,
    construction_demand_report integer,
    labor_availability_report integer,
    material_cost_index_report integer,
    weather_forecast_report integer,
    cam_report integer
);



--
-- Name: game_params_temperature; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.game_params_temperature (
    gameid integer NOT NULL,
    periodid smallint NOT NULL,
    adminid integer NOT NULL,
    temp_mean integer,
    temp_stddev integer,
    temp_min integer,
    temp_max integer
);



--
-- Name: grading_template; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.grading_template (
    adminid integer NOT NULL,
    gameid integer NOT NULL,
    template_string character varying(2048),
    template_string1 character varying(2048),
    template_string2 character varying(2048),
    template_string3 character varying(2048)
);



--
-- Name: inbox; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inbox (
    companyid integer NOT NULL,
    periodid integer NOT NULL,
    type smallint NOT NULL,
    link integer,
    sender character varying(80),
    message character varying(8192),
    subject character varying(80)
);



--
-- Name: interval_update_policy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.interval_update_policy (
    gameid integer NOT NULL,
    "interval" smallint NOT NULL,
    update_time time without time zone NOT NULL,
    next_update_date date
);



--
-- Name: job; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job (
    jobid integer NOT NULL,
    gameid integer NOT NULL,
    companyid integer,
    retentionid integer NOT NULL,
    job_type_id integer NOT NULL,
    liquidation_damages double precision,
    completion_date date,
    job_lu_size integer,
    lus_remaining integer,
    is_active boolean,
    is_auto boolean,
    direct_cost double precision,
    completion_deadline date,
    period_created smallint,
    period_awarded smallint DEFAULT 0,
    period_completed smallint,
    estimated_workdays integer,
    retained_amount bigint,
    method_scaling_factor real,
    change_order_lus integer,
    change_order_pay bigint,
    amount_retained_from_subs bigint,
    billed_to_date bigint,
    type_modifier smallint,
    customerid integer DEFAULT 0 NOT NULL,
    current_method smallint DEFAULT 1,
    method_changed_period smallint DEFAULT -1
);



--
-- Name: job_allowed_companies; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_allowed_companies (
    gameid integer NOT NULL,
    jobid integer NOT NULL,
    companyid integer NOT NULL
);



--
-- Name: job_financial_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_financial_info (
    gameid integer NOT NULL,
    companyid integer NOT NULL,
    periodid integer NOT NULL,
    jobid integer NOT NULL,
    payments_received double precision,
    retainage_received double precision,
    retainage_forfeited double precision,
    direct_labor_costs double precision,
    direct_materials_costs double precision,
    direct_equipment_costs double precision,
    subcontractor_costs double precision,
    field_overhead double precision,
    days_late integer
);



--
-- Name: job_type_req_mos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_type_req_mos (
    adminid integer NOT NULL,
    gameid integer NOT NULL,
    job_type_id smallint NOT NULL,
    skill_code character varying(20) NOT NULL,
    cu_amount integer NOT NULL,
    mos_amount integer NOT NULL
);



--
-- Name: loan; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.loan (
    loanid integer NOT NULL,
    gameid integer NOT NULL,
    companyid integer NOT NULL,
    periodid integer NOT NULL,
    amount double precision,
    balance double precision,
    payment_amount double precision,
    interest_paid double precision,
    payments_remaining smallint,
    is_approved boolean,
    repay_immediately boolean
);



--
-- Name: main_office_skills; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.main_office_skills (
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    skill_name character varying(80) NOT NULL,
    skill_code character varying(20) NOT NULL
);



--
-- Name: method; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.method (
    methodid smallint NOT NULL,
    activity_type_id smallint NOT NULL,
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    method_name character varying(20),
    productivity integer,
    laborcost integer,
    materialcost integer,
    equipmentcost integer,
    subcontractorcost integer,
    uncertainty_range integer,
    weather_sensitivity integer,
    subcontracted boolean NOT NULL
);



--
-- Name: mos_point_conversion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mos_point_conversion (
    gameid integer NOT NULL,
    adminid integer NOT NULL,
    employee_title character varying(80) NOT NULL,
    skill_code character varying(20) NOT NULL,
    base_to_mosp_ratio real NOT NULL
);



--
-- Name: mosp_cost; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mosp_cost (
    adminid integer NOT NULL,
    gameid integer NOT NULL,
    skill_code character varying(20) NOT NULL,
    price integer NOT NULL
);



--
-- Name: mosp_generation_jrnl; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mosp_generation_jrnl (
    gameid integer NOT NULL,
    employee_title character varying(20) NOT NULL,
    period_id integer NOT NULL,
    skill_code character varying(20) NOT NULL,
    points integer NOT NULL
);



--
-- Name: mosp_unfilled_jrnl; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mosp_unfilled_jrnl (
    gameid integer NOT NULL,
    companyid integer NOT NULL,
    period_id integer NOT NULL,
    skill_code character varying(20) NOT NULL,
    points integer NOT NULL
);



--
-- Name: negotiated_job; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.negotiated_job (
    customerid integer NOT NULL,
    companyid integer NOT NULL,
    jobid integer NOT NULL,
    gameid integer NOT NULL,
    agreed_cost double precision,
    agreed_percent_profit smallint,
    state smallint,
    customer_target_profit smallint,
    customer_haggling smallint
);



--
-- Name: overtime; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.overtime (
    activity_type_id smallint NOT NULL,
    gameid integer NOT NULL,
    jobid integer NOT NULL,
    overtime_days integer
);



--
-- Name: period; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.period (
    periodid integer NOT NULL,
    gameid integer NOT NULL,
    temperature integer,
    rainfall real,
    labor_availability integer,
    material_cost_index integer,
    number_of_new_jobs integer,
    number_of_new_heavy_jobs integer,
    workdays_first_month integer,
    workdays_second_month integer,
    raindays_first_month integer,
    raindays_second_month integer
);



--
-- Name: ratios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ratios (
    gameid integer NOT NULL,
    companyid integer NOT NULL,
    periodid integer NOT NULL,
    current_ratio real,
    receivable_turns real,
    payable_turns real,
    cash_turns real,
    cash_to_wip real,
    earnings_to_interest real,
    debt_to_equity real,
    gross_profit real,
    net_profit real,
    return_on_equity real,
    return_on_assets real,
    costs_to_sales real,
    ga_to_sales real,
    op_expense_to_sales real
);



--
-- Name: report_ledger; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_ledger (
    companyid integer NOT NULL,
    periodid integer NOT NULL,
    reportid integer NOT NULL,
    purchase_period integer NOT NULL
);



--
-- Name: retention; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.retention (
    retentionid smallint NOT NULL,
    percent_retention smallint,
    percent_of_job smallint
);



--
-- Name: schedule_estimated_methods; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.schedule_estimated_methods (
    gameid integer NOT NULL,
    jobid integer NOT NULL,
    activityid integer NOT NULL,
    methodid integer NOT NULL
);



--
-- Name: update_policy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.update_policy (
    gameid integer NOT NULL,
    policy smallint NOT NULL
);



--
-- Dependencies: 219
-- Data for Name: activity; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.activity (activity_type_id, gameid, jobid, periodid, lus_total, lus_completed, current_labor_cost, current_material_cost, current_equipment_cost, current_subcontractor_cost, is_active, overtime_days, lus_completed_this_period, accumulated_costs, current_method_used, amount_billed) FROM stdin;
\.


--
-- Dependencies: 220
-- Data for Name: activity_parameters; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.activity_parameters (activity_type_id, job_type_id, gameid, adminid, finish_to_finish_pct, finish_to_finish_activity, start_to_start_pct, start_to_start_activity) FROM stdin;
1	1	0	0	0	0	0	0
2	1	0	0	0	0	80	1
3	1	0	0	50	2	40	2
4	1	0	0	30	3	50	3
5	1	0	0	40	4	60	4
6	1	0	0	40	5	50	5
7	1	0	0	30	5	60	5
8	1	0	0	40	7	50	7
9	1	0	0	35	7	70	1
1	2	0	0	0	0	0	0
2	2	0	0	0	0	70	1
3	2	0	0	50	2	40	2
4	2	0	0	40	3	40	2
5	2	0	0	60	4	30	4
6	2	0	0	80	5	60	5
7	2	0	0	70	5	60	5
8	2	0	0	60	7	50	7
9	2	0	0	40	5	50	1
1	3	0	0	0	0	0	0
2	3	0	0	0	0	70	1
3	3	0	0	70	2	50	2
4	3	0	0	40	2	30	2
5	3	0	0	30	4	50	4
6	3	0	0	50	5	40	5
7	3	0	0	60	5	40	5
8	3	0	0	60	7	30	7
9	3	0	0	40	5	70	1
1	4	0	0	0	0	0	0
2	4	0	0	0	0	70	1
3	4	0	0	50	2	40	2
4	4	0	0	40	2	30	2
5	4	0	0	60	4	30	4
6	4	0	0	90	5	40	5
7	4	0	0	90	5	40	5
8	4	0	0	50	7	30	6
9	4	0	0	30	6	50	1
1	5	0	0	0	0	0	0
2	5	0	0	0	0	50	1
3	5	0	0	0	0	0	0
4	5	0	0	50	2	50	2
5	5	0	0	50	4	50	4
6	5	0	0	80	5	40	5
7	5	0	0	90	5	40	5
8	5	0	0	60	7	20	7
9	5	0	0	50	5	70	1
1	6	0	0	0	0	0	0
2	6	0	0	0	0	80	1
3	6	0	0	50	2	40	2
4	6	0	0	50	3	50	3
5	6	0	0	75	4	65	3
6	6	0	0	60	5	80	5
7	6	0	0	80	6	70	6
8	6	0	0	75	7	50	7
9	6	0	0	80	8	50	6
1	7	0	0	0	0	0	0
2	7	0	0	0	0	80	1
3	7	0	0	55	2	45	2
4	7	0	0	50	3	50	3
5	7	0	0	75	4	50	3
6	7	0	0	60	5	90	5
7	7	0	0	90	6	50	6
8	7	0	0	80	7	90	7
9	7	0	0	85	8	65	6
1	8	0	0	0	0	0	0
2	8	0	0	0	0	60	1
3	8	0	0	65	2	65	2
4	8	0	0	60	3	50	3
5	8	0	0	50	4	70	3
6	8	0	0	70	5	80	5
7	8	0	0	90	6	50	6
8	8	0	0	80	7	90	7
9	8	0	0	85	8	65	6
1	9	0	0	0	0	0	0
2	9	0	0	0	0	60	1
3	9	0	0	50	2	40	2
4	9	0	0	75	3	50	3
5	9	0	0	75	4	55	3
6	9	0	0	70	5	80	5
7	9	0	0	90	6	50	6
8	9	0	0	80	7	90	7
9	9	0	0	85	8	65	6
1	10	0	0	0	0	0	0
2	10	0	0	0	0	80	1
3	10	0	0	60	2	60	2
4	10	0	0	55	3	50	3
5	10	0	0	50	4	75	3
6	10	0	0	90	5	80	5
7	10	0	0	90	6	75	6
8	10	0	0	80	7	90	7
9	10	0	0	85	8	70	6
\.


--
-- Dependencies: 221
-- Data for Name: admin; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.admin (adminid, username, password, is_head_admin, email) FROM stdin;
0	admin	admin1357	t	\N
\.


--
-- Dependencies: 222
-- Data for Name: appraisal_metrics; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.appraisal_metrics (companyid, periodid, financial_liquidity, financial_success, bid_responsibility, pace, ethics, name_recognition, apartments, schools, offices, hospitals, industrial, highways, bridges, sitedevelopment, massexcavation, undergroundutilities) FROM stdin;
\.


--
-- Dependencies: 223
-- Data for Name: appraisal_metrics_updates; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.appraisal_metrics_updates (companyid, financial_liquidity, financial_success, bid_responsibility, pace, ethics, name_recognition, apartments, schools, offices, hospitals, industrial, highways, bridges, sitedevelopment, massexcavation, undergroundutilities) FROM stdin;
\.


--
-- Dependencies: 224
-- Data for Name: available_equipment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.available_equipment (gameid, adminid, equipment_title, equipment_cost_per_period, equipment_deploy_cost, equipment_decommission_cost, equipment_description, equipment_prcnt_raise, equipment_base_points, equipment_id) FROM stdin;
0	0	Asphalt Paver	25000	1000	500	desc	50	120	1
0	0	Bottom-Dump Truck	26000	1000	500	desc	50	120	2
0	0	Crane	27000	1000	500	desc	50	120	3
0	0	Crane Truck	28000	1000	500	desc	50	120	4
0	0	Crawler Loader	29000	1000	500	desc	50	120	5
0	0	Dozer	30000	1000	500	desc	50	120	6
0	0	Dump Truck	31000	1000	500	desc	50	120	7
0	0	Dump/Transfer Truck	32000	1000	500	desc	50	120	8
0	0	Electric Generator	33000	1000	500	desc	50	120	9
0	0	Elevating Scraper	34000	1000	500	desc	50	120	10
0	0	Excavator	35000	1000	500	desc	50	120	11
0	0	Forklift	36000	1000	500	desc	50	120	12
0	0	Fuel Truck	37000	1000	500	desc	50	120	13
0	0	Loader Backhoe	38000	1000	500	desc	50	120	14
0	0	Lowboy Truck	39000	1000	500	desc	50	120	15
0	0	Motor Grader	40000	1000	500	desc	50	120	16
0	0	Off-Highway Truck	41000	1000	500	desc	50	120	17
0	0	Scraper	42000	1000	500	desc	50	120	18
0	0	Service/Mechanic Truck	43000	1000	500	desc	50	120	19
0	0	Smooth Drum Roller	44000	1000	500	desc	50	120	20
0	0	Sweeper Truck	45000	1000	500	desc	50	120	21
0	0	Trencher	46000	1000	500	desc	50	120	22
0	0	Vacuum Truck	47000	1000	500	desc	50	120	23
0	0	Water Pump	48000	1000	500	desc	50	120	24
0	0	Water Truck	49000	1000	500	desc	50	120	25
0	0	Wheel Loader	50000	1000	500	desc	50	120	26
\.


--
-- Dependencies: 225
-- Data for Name: available_personnel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.available_personnel (gameid, adminid, category, employee_title, employee_cost_per_period, employee_hiring_cost, employee_firing_cost, employee_description, employee_prcnt_raise, employee_base_points) FROM stdin;
0	0	1	CEO	25000	25000	50000	desc	50	150
0	0	1	Vice President	20000	10000	40000	desc	50	120
0	0	1	Operations Manager	18000	9000	36000	desc	50	120
0	0	1	Comptroller	17000	8000	34000	desc	50	120
0	0	1	Project Executive	17000	10000	34000	desc	50	120
0	0	1	Chief Accountant	15000	3000	15000	desc	45	110
0	0	1	Project Accountant	12500	3000	3000	desc	45	100
0	0	1	Staff Accountant	7500	3000	3000	desc	30	90
0	0	1	Accounts Payable Clerk	5000	0	0	desc	20	80
0	0	1	Accounts Receivable Clerk	5000	0	0	desc	20	80
0	0	1	Receptionist	3200	0	0	desc	10	50
0	0	1	Secretary	4200	0	0	desc	12	75
0	0	1	Administrative Assistant	6500	3000	3000	desc	20	100
0	0	1	Office Engineer	9200	1000	5000	desc	20	100
0	0	1	Office Manager	10800	3000	3000	desc	20	100
0	0	1	Blue Print Clerk	3200	0	0	desc	10	50
0	0	1	Intern Student	4000	0	0	desc	0	50
0	0	1	Coop Student	5000	0	0	desc	0	60
0	0	1	Human Resources Manager	10800	3000	5000	desc	25	100
0	0	1	Marketing Manager	15000	3000	5000	desc	30	100
0	0	1	Marketing Coordinator	5800	2000	0	desc	20	80
0	0	2	Contracts Coordinator	5800	2000	0	desc	15	100
0	0	2	Project Coordinator	6500	3000	3000	desc	15	100
0	0	2	Senior Project Coordinator	7200	3000	3000	desc	25	100
0	0	2	Assistant Project Manager	10800	2000	5000	desc	20	90
0	0	2	Project Manager	12500	4000	5000	desc	35	100
0	0	2	Senior Project Manager	14500	5000	10000	desc	40	110
0	0	2	Assistant Estimator	10000	3000	5000	desc	20	90
0	0	2	Estimator	13500	4000	5000	desc	30	100
0	0	2	Senior Estimator	15000	5000	10000	desc	40	110
0	0	2	Estimating Coordinator	5800	2000	0	desc	20	80
0	0	2	Takeoff Person	8500	1000	5000	desc	20	80
0	0	2	Preconstruction Services Manager	13500	4000	5000	desc	40	100
0	0	2	Warehouse Manager	9200	1000	2000	desc	20	100
0	0	2	Warehouse Teamster	10800	1000	4000	desc	30	120
0	0	2	Warehouse Assistant	4200	0	0	desc	10	50
0	0	2	Cabinet Shop Manager	9200	1000	1000	desc	20	75
0	0	2	Concrete Services Manager	10800	1000	1000	desc	20	100
0	0	2	Safety Manager	10800	3000	5000	desc	25	100
0	0	2	Business Development Manager	15000	3000	5000	desc	30	100
\.


--
-- Dependencies: 226
-- Data for Name: balance_sheet; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.balance_sheet (gameid, companyid, periodid, cash_on_hand, current_accounts_receivable, retention_receivable, construction_equipment, autos, office_equipment, acc_depreciation, land, current_accounts_payable, retention_payable, income_taxes_payable, loan_balances, equity) FROM stdin;
\.


--
-- Dependencies: 227
-- Data for Name: bid; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bid (jobid, companyid, gameid, bid_amount, bid_amount_up, unitprice1, unitprice2, unitprice3, unitprice4, unitprice5, unitprice6, unitprice7, unitprice8, unitprice9, outcome_code) FROM stdin;
\.


--
-- Dependencies: 228
-- Data for Name: bid_method; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bid_method (jobid, companyid, gameid, bid_activity, bid_method) FROM stdin;
\.


--
-- Dependencies: 236
-- Data for Name: billing_breakdown; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.billing_breakdown (gameid, companyid, periodid, jobid, activityid, amount) FROM stdin;
\.


--
-- Dependencies: 237
-- Data for Name: billings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.billings (gameid, companyid, jobid, periodid, amount_billed, old_amount_billed, amount_paid, rejected) FROM stdin;
\.


--
-- Dependencies: 238
-- Data for Name: cash_flow; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cash_flow (gameid, companyid, periodid, amount, inflow, source) FROM stdin;
\.


--
-- Dependencies: 239
-- Data for Name: ccc_job_type_preferences; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ccc_job_type_preferences (cccid, job_type_id, preference) FROM stdin;
\.


--
-- Dependencies: 240
-- Data for Name: company; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.company (companyid, gameid, title, username, password, license, cash_on_hand, wip_limit, wip_limit_percent, per_job_limit, per_job_wip_percent, wip, missionstatement, corevalues, companyurl) FROM stdin;
\.


--
-- Dependencies: 241
-- Data for Name: company_members; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.company_members (companyid, gameid, firstname, lastname, email) FROM stdin;
\.


--
-- Dependencies: 242
-- Data for Name: company_personnel; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.company_personnel (gameid, companyid, periodid, employee_title, employee_type_count, employee_type_adds, employee_type_loss) FROM stdin;
\.


--
-- Dependencies: 243
-- Data for Name: component; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.component (gameid, estimating, autobilling) FROM stdin;
\.


--
-- Dependencies: 244
-- Data for Name: computer_controlled_contractors; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.computer_controlled_contractors (cccid, gameid, fitness, name, current) FROM stdin;
\.


--
-- Dependencies: 245
-- Data for Name: contract_reports; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.contract_reports (gameid, companyid, periodid, jobid, previous_cost, current_cost, billed_to_date, est_cost_to_complete, revenue_recorded, big_computes_est_cost) FROM stdin;
\.


--
-- Dependencies: 246
-- Data for Name: current_methods; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.current_methods (gameid, jobid, activity_type_id, method_id) FROM stdin;
\.


--
-- Dependencies: 247
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.customer (customerid, customer_name, fin_liq_factor, fin_liq_level, fin_suc_factor, fin_suc_level, responsibility_factor, responsibility_level, pace_factor, pace_level, ethics_factor, ethics_level, name_rec_factor, name_rec_level, job_type_factor, job_type_level) FROM stdin;
1	Private Company 1	5	30	5	30	5	30	5	30	5	30	70	40	5	10
2	Private Company 2	0	0	0	0	0	0	0	0	0	0	100	50	0	0
3	Private Company 3	25	50	25	50	0	0	0	0	0	0	50	30	0	0
4	Private Company 4	0	0	0	0	20	40	30	40	0	0	50	45	0	0
5	Private Company 5	0	0	0	0	0	0	10	10	0	0	50	30	40	10
6	Private Company 6	10	30	0	0	0	0	0	0	40	50	50	50	0	0
7	Private Company 7	0	0	0	0	0	0	0	0	0	0	50	50	50	50
8	Private Company 8	0	0	0	0	0	0	0	0	0	0	100	90	0	0
9	Private Company 9	5	90	15	50	0	0	20	30	0	0	50	70	10	65
10	Private Company 10	8	75	9	75	7	75	9	75	7	75	50	75	9	75
\.


--
-- Dependencies: 248
-- Data for Name: customer_company_opinions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.customer_company_opinions (customerid, companyid, opinion) FROM stdin;
\.


--
-- Dependencies: 249
-- Data for Name: dates_update_policy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dates_update_policy (gameid, date, update_time, date_hash, complete) FROM stdin;
\.


--
-- Dependencies: 250
-- Data for Name: days_update_policy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.days_update_policy (gameid, day, update_time) FROM stdin;
\.


--
-- Dependencies: 251
-- Data for Name: equip_network_dep; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.equip_network_dep (activity_type_id, job_type_id, gameid, adminid, equip_type, quantity_of_equip, percent_time) FROM stdin;
\.


--
-- Dependencies: 253
-- Data for Name: estimates_available; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.estimates_available (gameid, companyid, jobid) FROM stdin;
\.


--
-- Dependencies: 254
-- Data for Name: estimates_per_period; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.estimates_per_period (gameid, estimates) FROM stdin;
\.


--
-- Dependencies: 255
-- Data for Name: financial_report; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.financial_report (gameid, companyid, periodid, interest_paid, bidding_expenses, consulting_expenses, office_overhead, legal_fees, settlement, previous_cash_on_hand, loan_principle_payments) FROM stdin;
\.


--
-- Dependencies: 256
-- Data for Name: game; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game (gameid, gamename, adminid, current_period, start_year, is_active, current_job_number, gametype) FROM stdin;
0	default	0	0	2000-01-01	f	0	\N
\.


--
-- Dependencies: 257
-- Data for Name: game_params_ccc; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_ccc (gameid, adminid, min_number_cccs, max_number_cccs, min_ccc_bids_per_job, max_ccc_bids_per_job, ccc_turnover, upper_profit_limit, lower_profit_limit) FROM stdin;
0	0	10	20	2	10	25	20	10
\.


--
-- Dependencies: 258
-- Data for Name: game_params_job_size; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_job_size (gameid, job_type_id, adminid, job_size_mean, job_size_stddev, job_size_min, job_size_max) FROM stdin;
0	1	0	22000	3000	15000	35000
0	2	0	37000	3500	27000	50000
0	3	0	35000	3000	27000	50000
0	4	0	43000	3000	33000	60000
0	5	0	44000	3500	33000	65000
0	6	0	48000	3000	30000	60000
0	7	0	40000	3500	24000	50000
0	8	0	32000	3000	22000	40000
0	9	0	54000	3000	35000	65000
0	10	0	29000	3500	15000	35000
\.


--
-- Dependencies: 259
-- Data for Name: game_params_jobs_per_period; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_jobs_per_period (gameid, periodid, adminid, number_jobs_mean, number_jobs_stddev, number_jobs_min, number_jobs_max, number_hc_jobs_min, number_hc_jobs_max) FROM stdin;
0	1	0	8	2	6	12	6	12
0	2	0	6	1	2	10	2	10
0	3	0	5	1	3	7	3	7
0	4	0	2	1	1	3	1	3
0	5	0	6	3	1	9	1	9
0	6	0	11	1	8	12	8	12
0	7	0	2	2	1	5	1	5
0	8	0	5	4	3	10	3	10
0	9	0	4	1	3	5	3	5
0	10	0	4	2	4	8	4	8
0	11	0	10	3	7	12	7	12
0	12	0	4	1	3	6	3	6
\.


--
-- Dependencies: 260
-- Data for Name: game_params_labor_avail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_labor_avail (gameid, periodid, adminid, labor_avail_mean, labor_avail_stddev, labor_avail_min, labor_avail_max) FROM stdin;
0	1	0	100	0	92	108
0	2	0	100	2	92	108
0	3	0	100	1	92	108
0	4	0	100	3	92	108
0	5	0	100	3	92	108
0	6	0	100	4	92	108
0	7	0	100	5	92	108
0	8	0	100	3	92	108
0	9	0	100	1	92	108
0	10	0	100	1	92	108
0	11	0	100	2	92	108
0	12	0	100	1	92	108
\.


--
-- Dependencies: 261
-- Data for Name: game_params_liquidated_damages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_liquidated_damages (gameid, adminid, mean_dc_percent, stddev_dc_percent, min_dc_percent, max_dc_percent, percent_jobs, faster_jobs_increase, larger_jobs_increase) FROM stdin;
0	0	0.1	0.05	0.01	1	100	t	t
\.


--
-- Dependencies: 262
-- Data for Name: game_params_material_cost_idx; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_material_cost_idx (gameid, periodid, adminid, material_cost_index_mean, material_cost_index_stddev, material_cost_index_min, material_cost_index_max) FROM stdin;
0	1	0	100	0	92	108
0	2	0	100	2	92	108
0	3	0	100	1	92	108
0	4	0	100	3	92	108
0	5	0	100	3	92	108
0	6	0	100	4	92	108
0	7	0	100	5	92	108
0	8	0	100	3	92	108
0	9	0	100	1	92	108
0	10	0	100	1	92	108
0	11	0	100	2	92	108
0	12	0	100	1	92	108
\.


--
-- Dependencies: 263
-- Data for Name: game_params_misc; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_misc (gameid, adminid, workdays_per_month, standard_hrs_per_day, overtime_hrs_per_day, bid_expense_percent, loan_interest_per_year, max_jobs_per_team, max_loans_per_team, max_loan_amount, loan_repayment_periods, overtime_expense_factor, max_overbilling_percent, income_tax_percent, depreciation_percent, overbilling_eff_eth_lim, bond_reeval_frequency) FROM stdin;
0	0	20	8	5	3	12	10	3	10000000	7	2	30	35	10	15	3
\.


--
-- Dependencies: 264
-- Data for Name: game_params_negotiation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_negotiation (gameid, adminid, word_of_mouth_rate_min, word_of_mouth_rate_max, word_of_mouth_insty_min, word_of_mouth_insty_max, pct_closed_bids_to_neg, mean_pct_profit, std_dev_pct_profit, min_pct_profit, max_pct_profit, lkhd_customer_breaks_neg, est_eff_ethics_limit, customer_haggling_mean, customer_haggling_std_dev, customer_haggling_min, customer_haggling_max) FROM stdin;
0	0	0	20	10	40	30	5	1.25	1	10	20	20	75	10	50	99
\.


--
-- Dependencies: 265
-- Data for Name: game_params_overhead; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_overhead (gameid, adminid, field_overhead_fixed, field_overhead_variable, office_overhead_fixed) FROM stdin;
0	0	900	1	40000
\.


--
-- Dependencies: 266
-- Data for Name: game_params_percent_takeoff; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_percent_takeoff (gameid, adminid, job_type_id, activity_type_id, percent_takeoff) FROM stdin;
0	0	1	1	11
0	0	1	2	11
0	0	1	3	11
0	0	1	4	12
0	0	1	5	11
0	0	1	6	11
0	0	1	7	11
0	0	1	8	11
0	0	1	9	11
0	0	2	1	11
0	0	2	2	11
0	0	2	3	9
0	0	2	4	11
0	0	2	5	11
0	0	2	6	12
0	0	2	7	11
0	0	2	8	12
0	0	2	9	12
0	0	3	1	11
0	0	3	2	11
0	0	3	3	7
0	0	3	4	11
0	0	3	5	11
0	0	3	6	11
0	0	3	7	12
0	0	3	8	14
0	0	3	9	12
0	0	4	1	10
0	0	4	2	10
0	0	4	3	11
0	0	4	4	10
0	0	4	5	11
0	0	4	6	11
0	0	4	7	11
0	0	4	8	13
0	0	4	9	13
0	0	5	1	10
0	0	5	2	12
0	0	5	3	0
0	0	5	4	12
0	0	5	5	12
0	0	5	6	12
0	0	5	7	12
0	0	5	8	12
0	0	5	9	18
0	0	6	1	5
0	0	6	2	8
0	0	6	3	7
0	0	6	4	2
0	0	6	5	3
0	0	6	6	11
0	0	6	7	23
0	0	6	8	35
0	0	6	9	6
0	0	7	1	10
0	0	7	2	9
0	0	7	3	7
0	0	7	4	4
0	0	7	5	48
0	0	7	6	8
0	0	7	7	3
0	0	7	8	5
0	0	7	9	6
0	0	8	1	11
0	0	8	2	18
0	0	8	3	4
0	0	8	4	27
0	0	8	5	8
0	0	8	6	5
0	0	8	7	6
0	0	8	8	9
0	0	8	9	12
0	0	9	1	11
0	0	9	2	14
0	0	9	3	32
0	0	9	4	4
0	0	9	5	6
0	0	9	6	15
0	0	9	7	3
0	0	9	8	2
0	0	9	9	13
0	0	10	1	4
0	0	10	2	7
0	0	10	3	18
0	0	10	4	32
0	0	10	5	5
0	0	10	6	21
0	0	10	7	2
0	0	10	8	3
0	0	10	9	8
\.


--
-- Dependencies: 267
-- Data for Name: game_params_rainfall; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_rainfall (gameid, periodid, adminid, rainfall_mean, rainfall_stddev, rainfall_min, rainfall_max) FROM stdin;
0	1	0	3	1	1	4
0	2	0	2	2	0	4
0	3	0	1	2	1	3
0	4	0	0	1	0	2
0	5	0	0	1	0	2
0	6	0	0	0	0	1
0	7	0	0	0	0	1
0	8	0	1	0	0	1
0	9	0	2	1	0	2
0	10	0	1	2	0	3
0	11	0	2	1	1	2
0	12	0	3	1	0	4
\.


--
-- Dependencies: 268
-- Data for Name: game_params_report_costs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_report_costs (gameid, adminid, complete_list_report, construction_demand_report, labor_availability_report, material_cost_index_report, weather_forecast_report, cam_report) FROM stdin;
0	0	1000	500	500	500	500	1000
\.


--
-- Dependencies: 269
-- Data for Name: game_params_temperature; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.game_params_temperature (gameid, periodid, adminid, temp_mean, temp_stddev, temp_min, temp_max) FROM stdin;
0	1	0	33	3	31	36
0	2	0	37	3	33	40
0	3	0	43	3	41	46
0	4	0	58	5	50	64
0	5	0	65	3	60	70
0	6	0	70	5	60	80
0	7	0	80	5	70	90
0	8	0	80	5	70	90
0	9	0	72	3	65	80
0	10	0	60	3	50	55
0	11	0	55	5	40	50
0	12	0	40	3	37	46
\.


--
-- Dependencies: 270
-- Data for Name: grading_template; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.grading_template (adminid, gameid, template_string, template_string1, template_string2, template_string3) FROM stdin;
\.


--
-- Dependencies: 271
-- Data for Name: inbox; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.inbox (companyid, periodid, type, link, sender, message, subject) FROM stdin;
\.


--
-- Dependencies: 272
-- Data for Name: interval_update_policy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.interval_update_policy (gameid, "interval", update_time, next_update_date) FROM stdin;
\.


--
-- Dependencies: 273
-- Data for Name: job; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job (jobid, gameid, companyid, retentionid, job_type_id, liquidation_damages, completion_date, job_lu_size, lus_remaining, is_active, is_auto, direct_cost, completion_deadline, period_created, period_completed, estimated_workdays, retained_amount, method_scaling_factor, change_order_lus, change_order_pay, amount_retained_from_subs, billed_to_date, type_modifier, customerid) FROM stdin;
\.


--
-- Dependencies: 274
-- Data for Name: job_allowed_companies; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_allowed_companies (gameid, jobid, companyid) FROM stdin;
\.


--
-- Dependencies: 275
-- Data for Name: job_financial_info; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_financial_info (gameid, companyid, periodid, jobid, payments_received, retainage_received, retainage_forfeited, direct_labor_costs, direct_materials_costs, direct_equipment_costs, subcontractor_costs, field_overhead, days_late) FROM stdin;
\.


--
-- Dependencies: 276
-- Data for Name: job_type_req_mos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_type_req_mos (adminid, gameid, job_type_id, skill_code, cu_amount, mos_amount) FROM stdin;
0	0	1	CL	15000	5
0	0	1	PL	15000	5
0	0	1	GFA	15000	10
0	0	1	SFA	15000	0
0	0	1	BE	15000	10
0	0	1	AE	15000	0
0	0	1	BS	15000	10
0	0	1	AS	15000	0
0	0	1	BSF	15000	10
0	0	1	ASF	15000	0
0	0	1	M	15000	10
0	0	1	AM	15000	0
0	0	1	PC	15000	5
0	0	1	S	15000	0
0	0	1	AB	15000	10
0	0	1	MK	15000	0
0	0	1	OW	15000	10
0	0	1	PW	15000	0
0	0	2	CL	15000	5
0	0	2	PL	15000	5
0	0	2	GFA	15000	10
0	0	2	SFA	15000	0
0	0	2	BE	15000	10
0	0	2	AE	15000	0
0	0	2	BS	15000	10
0	0	2	AS	15000	0
0	0	2	BSF	15000	10
0	0	2	ASF	15000	0
0	0	2	M	15000	10
0	0	2	AM	15000	0
0	0	2	PC	15000	5
0	0	2	S	15000	0
0	0	2	AB	15000	10
0	0	2	MK	15000	0
0	0	2	OW	15000	10
0	0	2	PW	15000	0
0	0	3	CL	15000	5
0	0	3	PL	15000	5
0	0	3	GFA	15000	10
0	0	3	SFA	15000	0
0	0	3	BE	15000	10
0	0	3	AE	15000	0
0	0	3	BS	15000	10
0	0	3	AS	15000	0
0	0	3	BSF	15000	10
0	0	3	ASF	15000	0
0	0	3	M	15000	10
0	0	3	AM	15000	0
0	0	3	PC	15000	5
0	0	3	S	15000	0
0	0	3	AB	15000	10
0	0	3	MK	15000	0
0	0	3	OW	15000	10
0	0	3	PW	15000	0
0	0	4	CL	15000	5
0	0	4	PL	15000	5
0	0	4	GFA	15000	10
0	0	4	SFA	15000	0
0	0	4	BE	15000	10
0	0	4	AE	15000	0
0	0	4	BS	15000	10
0	0	4	AS	15000	0
0	0	4	BSF	15000	10
0	0	4	ASF	15000	0
0	0	4	M	15000	10
0	0	4	AM	15000	0
0	0	4	PC	15000	5
0	0	4	S	15000	0
0	0	4	AB	15000	10
0	0	4	MK	15000	0
0	0	4	OW	15000	10
0	0	4	PW	15000	0
0	0	5	CL	15000	5
0	0	5	PL	15000	5
0	0	5	GFA	15000	10
0	0	5	SFA	15000	0
0	0	5	BE	15000	10
0	0	5	AE	15000	0
0	0	5	BS	15000	10
0	0	5	AS	15000	0
0	0	5	BSF	15000	10
0	0	5	ASF	15000	0
0	0	5	M	15000	10
0	0	5	AM	15000	0
0	0	5	PC	15000	5
0	0	5	S	15000	0
0	0	5	AB	15000	10
0	0	5	MK	15000	0
0	0	5	OW	15000	10
0	0	5	PW	15000	0
0	0	6	CL	15000	5
0	0	6	PL	15000	5
0	0	6	GFA	15000	10
0	0	6	SFA	15000	0
0	0	6	BE	15000	10
0	0	6	AE	15000	0
0	0	6	BS	15000	10
0	0	6	AS	15000	0
0	0	6	BSF	15000	10
0	0	6	ASF	15000	0
0	0	6	M	15000	10
0	0	6	AM	15000	0
0	0	6	PC	15000	5
0	0	6	S	15000	0
0	0	6	AB	15000	10
0	0	6	MK	15000	0
0	0	6	OW	15000	10
0	0	6	PW	15000	0
0	0	7	CL	15000	5
0	0	7	PL	15000	5
0	0	7	GFA	15000	10
0	0	7	SFA	15000	0
0	0	7	BE	15000	10
0	0	7	AE	15000	0
0	0	7	BS	15000	10
0	0	7	AS	15000	0
0	0	7	BSF	15000	10
0	0	7	ASF	15000	0
0	0	7	M	15000	10
0	0	7	AM	15000	0
0	0	7	PC	15000	5
0	0	7	S	15000	0
0	0	7	AB	15000	10
0	0	7	MK	15000	0
0	0	7	OW	15000	10
0	0	7	PW	15000	0
0	0	8	CL	15000	5
0	0	8	PL	15000	5
0	0	8	GFA	15000	10
0	0	8	SFA	15000	0
0	0	8	BE	15000	10
0	0	8	AE	15000	0
0	0	8	BS	15000	10
0	0	8	AS	15000	0
0	0	8	BSF	15000	10
0	0	8	ASF	15000	0
0	0	8	M	15000	10
0	0	8	AM	15000	0
0	0	8	PC	15000	5
0	0	8	S	15000	0
0	0	8	AB	15000	10
0	0	8	MK	15000	0
0	0	8	OW	15000	10
0	0	8	PW	15000	0
0	0	9	CL	15000	5
0	0	9	PL	15000	5
0	0	9	GFA	15000	10
0	0	9	SFA	15000	0
0	0	9	BE	15000	10
0	0	9	AE	15000	0
0	0	9	BS	15000	10
0	0	9	AS	15000	0
0	0	9	BSF	15000	10
0	0	9	ASF	15000	0
0	0	9	M	15000	10
0	0	9	AM	15000	0
0	0	9	PC	15000	5
0	0	9	S	15000	0
0	0	9	AB	15000	10
0	0	9	MK	15000	0
0	0	9	OW	15000	10
0	0	9	PW	15000	0
0	0	10	CL	15000	5
0	0	10	PL	15000	5
0	0	10	GFA	15000	10
0	0	10	SFA	15000	0
0	0	10	BE	15000	10
0	0	10	AE	15000	0
0	0	10	BS	15000	10
0	0	10	AS	15000	0
0	0	10	BSF	15000	10
0	0	10	ASF	15000	0
0	0	10	M	15000	10
0	0	10	AM	15000	0
0	0	10	PC	15000	5
0	0	10	S	15000	0
0	0	10	AB	15000	10
0	0	10	MK	15000	0
0	0	10	OW	15000	10
0	0	10	PW	15000	0
\.


--
-- Dependencies: 277
-- Data for Name: loan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.loan (loanid, gameid, companyid, periodid, amount, balance, payment_amount, interest_paid, payments_remaining, is_approved, repay_immediately) FROM stdin;
\.


--
-- Dependencies: 278
-- Data for Name: main_office_skills; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.main_office_skills (gameid, adminid, skill_name, skill_code) FROM stdin;
0	0	Company Leadership	CL
0	0	Project Leadership	PL
0	0	General Finance and Accounting	GFA
0	0	Special Finance and Accounting	SFA
0	0	Basic Estimating	BE
0	0	Advanced Estimating	AE
0	0	Basic Scheduling	BS
0	0	Advanced Scheduling	AS
0	0	Basic Safety	BSF
0	0	Advanced Safety	ASF
0	0	Management	M
0	0	Advanced Management	AM
0	0	Project Coordination	PC
0	0	Staffing	S
0	0	Accounts and Billing	AB
0	0	Marketing	MK
0	0	Office Work	OW
0	0	Physical Work	PW
\.


--
-- Dependencies: 279
-- Data for Name: method; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.method (methodid, activity_type_id, gameid, adminid, method_name, productivity, laborcost, materialcost, equipmentcost, subcontractorcost, uncertainty_range, weather_sensitivity, subcontracted) FROM stdin;
1	1	0	0	Excavation1	8	11	10	9	0	5	3	f
2	1	0	0	Excavation2	12	18	12	11	0	15	3	f
3	1	0	0	Excavation3	15	18	22	12	0	10	0	f
4	1	0	0	Excavation4	22	30	29	14	0	15	2	f
5	1	0	0	Subcontractor	30	0	0	0	95	0	0	t
1	2	0	0	Foundation1	7	15	12	4	0	15	2	f
2	2	0	0	Foundation2	11	27	15	5	0	5	1	f
3	2	0	0	Foundation3	13	25	25	7	0	10	3	f
4	2	0	0	Foundation4	20	47	30	9	0	15	3	f
5	2	0	0	Subcontractor	26	0	0	0	113	0	0	t
1	3	0	0	Basement1	6	9	11	3	0	10	3	f
2	3	0	0	Basement2	8	30	16	4	0	5	0	f
3	3	0	0	Basement3	12	35	35	6	0	10	1	f
4	3	0	0	Basement4	17	59	40	6	0	5	2	f
5	3	0	0	Subcontractor	25	0	0	0	157	0	0	t
1	4	0	0	Framing1	5	20	16	1	0	15	3	f
2	4	0	0	Framing2	8	37	21	2	0	12	3	f
3	4	0	0	Framing3	11	45	35	2	0	3	0	f
4	4	0	0	Framing4	15	67	42	3	0	5	2	f
5	4	0	0	Subcontractor	21	0	0	0	159	0	0	t
1	5	0	0	Closure1	5	35	10	2	0	10	0	f
2	5	0	0	Closure2	8	37	35	2	0	25	2	f
3	5	0	0	Closure3	10	48	42	3	0	15	3	f
4	5	0	0	Closure4	17	90	63	4	0	12	3	f
5	5	0	0	Subcontractor	20	0	0	0	189	0	0	t
1	6	0	0	Roof1	4	22	22	3	0	25	3	f
2	6	0	0	Roof2	6	36	30	3	0	10	1	f
3	6	0	0	Roof3	9	45	55	4	0	3	0	f
4	6	0	0	Roof4	14	80	75	5	0	5	1	f
5	6	0	0	Subcontractor	17	0	0	0	197	0	0	t
1	7	0	0	Siding1	4	30	25	1	0	10	3	f
2	7	0	0	Siding2	7	63	33	1	0	15	2	f
3	7	0	0	Siding3	8	90	20	2	0	25	1	f
4	7	0	0	Siding4	13	100	78	2	0	10	0	f
5	7	0	0	Subcontractor	15	0	0	0	209	0	0	t
1	8	0	0	Finishing1	3	35	40	2	0	10	3	f
2	8	0	0	Finishing2	5	50	80	2	0	15	2	f
3	8	0	0	Finishing3	7	90	40	3	0	13	1	f
4	8	0	0	Finishing4	11	80	125	3	0	15	0	f
5	8	0	0	Subcontractor	14	0	0	0	268	0	0	t
1	9	0	0	Subcontractor1	2	0	0	0	65	0	0	t
2	9	0	0	Subcontractor2	3	0	0	0	90	0	0	t
3	9	0	0	Subcontractor3	5	0	0	0	150	0	0	t
4	9	0	0	Subcontractor4	7	0	0	0	220	0	0	t
5	9	0	0	Subcontractor5	9	0	0	0	300	0	0	t
1	10	0	0	Clear&Grub1	8	11	2	25	0	5	3	f
2	10	0	0	Clear&Grub2	12	15	2	28	0	15	3	f
3	10	0	0	Clear&Grub3	15	18	3	29	0	10	0	f
4	10	0	0	Clear&Grub4	22	22	3	32	0	15	2	f
5	10	0	0	Clear&Grub5	28	25	4	35	0	10	3	f
1	11	0	0	RoughGrading1	7	7	3	30	0	15	2	f
2	11	0	0	RoughGrading2	11	12	3	33	0	5	1	f
3	11	0	0	RoughGrading3	13	15	3	34	0	10	3	f
4	11	0	0	RoughGrading4	20	19	4	37	0	15	3	f
5	11	0	0	RoughGrading5	26	23	4	40	0	10	3	f
1	12	0	0	Excavation1	6	9	5	34	0	10	3	f
2	12	0	0	Excavation2	8	12	6	38	0	5	0	f
3	12	0	0	Excavation3	12	15	7	39	0	10	1	f
4	12	0	0	Excavation4	17	18	7	42	0	5	2	f
5	12	0	0	Excavation5	22	21	8	43	0	10	3	f
1	13	0	0	UndergroundPipe1	5	15	26	12	0	15	3	f
2	13	0	0	UndergroundPipe2	8	17	29	13	0	12	3	f
3	13	0	0	UndergroundPipe3	11	20	35	15	0	3	1	f
4	13	0	0	Subcontractor1	15	0	0	0	90	0	0	t
5	13	0	0	Subcontractor2	21	0	0	0	105	0	0	t
1	14	0	0	Concrete1	5	25	22	27	0	10	0	f
2	14	0	0	Concrete2	8	27	25	29	0	25	2	f
3	14	0	0	Concrete3	10	31	28	35	0	15	3	f
4	14	0	0	Subcontractor1	17	0	0	0	120	0	0	t
5	14	0	0	Subcontractor2	20	0	0	0	140	0	0	t
1	15	0	0	Backfill&Compact1	4	14	2	28	0	25	3	f
2	15	0	0	Backfill&Compact2	6	16	3	31	0	10	1	f
3	15	0	0	Backfill&Compact3	9	21	4	32	0	3	0	f
4	15	0	0	Backfill&Compact4	14	25	5	35	0	5	1	f
5	15	0	0	Backfill&Compact5	17	29	5	39	0	10	2	f
1	16	0	0	AggBase1	4	7	18	10	0	10	3	f
2	16	0	0	AggBase2	7	10	19	11	0	15	2	f
3	16	0	0	AggBase3	8	12	20	11	0	25	1	f
4	16	0	0	AggBase4	13	15	22	12	0	10	0	f
5	16	0	0	AggBase5	18	19	23	13	0	10	1	f
1	17	0	0	Paving1	3	10	20	27	0	10	3	f
2	17	0	0	Paving2	5	15	22	29	0	15	2	f
3	17	0	0	Paving3	7	19	23	30	0	13	1	f
4	17	0	0	Paving4	11	21	24	31	0	15	0	f
5	17	0	0	Paving5	16	26	24	33	0	10	3	f
1	18	0	0	FinishGrade1	3	6	3	19	0	10	3	f
2	18	0	0	FinishGrade2	5	9	3	21	0	15	2	f
3	18	0	0	FinishGrade3	7	12	4	24	0	13	1	f
4	18	0	0	FinishGrade4	11	13	4	26	0	15	0	f
5	18	0	0	FinishGrade5	16	14	5	27	0	10	3	f
\.


--
-- Dependencies: 280
-- Data for Name: mos_point_conversion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mos_point_conversion (gameid, adminid, employee_title, skill_code, base_to_mosp_ratio) FROM stdin;
0	0	CEO	CL	1
0	0	CEO	PL	1
0	0	CEO	GFA	1
0	0	CEO	SFA	0.5
0	0	CEO	BE	1
0	0	CEO	AE	0.33
0	0	CEO	BS	1
0	0	CEO	AS	0.5
0	0	CEO	BSF	1
0	0	CEO	ASF	0.33
0	0	CEO	M	1
0	0	CEO	AM	1
0	0	CEO	PC	1
0	0	CEO	S	1
0	0	CEO	AB	1
0	0	CEO	MK	1
0	0	CEO	OW	1
0	0	CEO	PW	0.2
0	0	Vice President	CL	1
0	0	Vice President	PL	0.66
0	0	Vice President	GFA	1
0	0	Vice President	SFA	0.33
0	0	Vice President	BE	0.5
0	0	Vice President	AE	0.25
0	0	Vice President	BS	0.5
0	0	Vice President	AS	0.25
0	0	Vice President	BSF	1
0	0	Vice President	ASF	0.33
0	0	Vice President	M	1
0	0	Vice President	AM	1
0	0	Vice President	PC	1
0	0	Vice President	S	1
0	0	Vice President	AB	1
0	0	Vice President	MK	1
0	0	Vice President	OW	1
0	0	Vice President	PW	0.2
0	0	Operations Manager	CL	0.5
0	0	Operations Manager	PL	1
0	0	Operations Manager	GFA	1
0	0	Operations Manager	SFA	0.33
0	0	Operations Manager	BE	1
0	0	Operations Manager	AE	0.33
0	0	Operations Manager	BS	1
0	0	Operations Manager	AS	1
0	0	Operations Manager	BSF	1
0	0	Operations Manager	ASF	0.5
0	0	Operations Manager	M	1
0	0	Operations Manager	AM	1
0	0	Operations Manager	PC	1
0	0	Operations Manager	S	1
0	0	Operations Manager	AB	1
0	0	Operations Manager	MK	0.33
0	0	Operations Manager	OW	1
0	0	Operations Manager	PW	0.2
0	0	Comptroller	CL	0.33
0	0	Comptroller	PL	0.5
0	0	Comptroller	GFA	1
0	0	Comptroller	SFA	1
0	0	Comptroller	BE	0.33
0	0	Comptroller	AE	0.2
0	0	Comptroller	BS	0.33
0	0	Comptroller	AS	0.2
0	0	Comptroller	BSF	0.25
0	0	Comptroller	ASF	0.1
0	0	Comptroller	M	1
0	0	Comptroller	AM	0.33
0	0	Comptroller	PC	0.5
0	0	Comptroller	S	0.5
0	0	Comptroller	AB	1
0	0	Comptroller	MK	0.1
0	0	Comptroller	OW	1
0	0	Comptroller	PW	0.2
0	0	Project Executive	CL	0.33
0	0	Project Executive	PL	1
0	0	Project Executive	GFA	1
0	0	Project Executive	SFA	0.25
0	0	Project Executive	BE	1
0	0	Project Executive	AE	1
0	0	Project Executive	BS	1
0	0	Project Executive	AS	1
0	0	Project Executive	BSF	1
0	0	Project Executive	ASF	0.5
0	0	Project Executive	M	1
0	0	Project Executive	AM	1
0	0	Project Executive	PC	1
0	0	Project Executive	S	0.5
0	0	Project Executive	AB	1
0	0	Project Executive	MK	0.25
0	0	Project Executive	OW	1
0	0	Project Executive	PW	0.2
0	0	Chief Accountant	CL	0.25
0	0	Chief Accountant	PL	0.33
0	0	Chief Accountant	GFA	1
0	0	Chief Accountant	SFA	0.66
0	0	Chief Accountant	BE	0.33
0	0	Chief Accountant	AE	0.2
0	0	Chief Accountant	BS	0.33
0	0	Chief Accountant	AS	0.2
0	0	Chief Accountant	BSF	0.25
0	0	Chief Accountant	ASF	0.1
0	0	Chief Accountant	M	0.66
0	0	Chief Accountant	AM	0.2
0	0	Chief Accountant	PC	0.33
0	0	Chief Accountant	S	0.33
0	0	Chief Accountant	AB	1
0	0	Chief Accountant	MK	0.1
0	0	Chief Accountant	OW	1
0	0	Chief Accountant	PW	0.2
0	0	Project Accountant	CL	0.2
0	0	Project Accountant	PL	0.33
0	0	Project Accountant	GFA	1
0	0	Project Accountant	SFA	0.5
0	0	Project Accountant	BE	0.25
0	0	Project Accountant	AE	0.1
0	0	Project Accountant	BS	0.25
0	0	Project Accountant	AS	0.1
0	0	Project Accountant	BSF	0.2
0	0	Project Accountant	ASF	0.1
0	0	Project Accountant	M	0.5
0	0	Project Accountant	AM	0.1
0	0	Project Accountant	PC	0.2
0	0	Project Accountant	S	0.2
0	0	Project Accountant	AB	1
0	0	Project Accountant	MK	0.1
0	0	Project Accountant	OW	1
0	0	Project Accountant	PW	0.2
0	0	Staff Accountant	CL	0
0	0	Staff Accountant	PL	0.1
0	0	Staff Accountant	GFA	0.66
0	0	Staff Accountant	SFA	0.25
0	0	Staff Accountant	BE	0.2
0	0	Staff Accountant	AE	0
0	0	Staff Accountant	BS	0.2
0	0	Staff Accountant	AS	0
0	0	Staff Accountant	BSF	0.2
0	0	Staff Accountant	ASF	0
0	0	Staff Accountant	M	0.5
0	0	Staff Accountant	AM	0
0	0	Staff Accountant	PC	0.2
0	0	Staff Accountant	S	0.1
0	0	Staff Accountant	AB	1
0	0	Staff Accountant	MK	0.1
0	0	Staff Accountant	OW	1
0	0	Staff Accountant	PW	0.2
0	0	Accounts Payable Clerk	CL	0
0	0	Accounts Payable Clerk	PL	0
0	0	Accounts Payable Clerk	GFA	0.33
0	0	Accounts Payable Clerk	SFA	0
0	0	Accounts Payable Clerk	BE	0.2
0	0	Accounts Payable Clerk	AE	0
0	0	Accounts Payable Clerk	BS	0.2
0	0	Accounts Payable Clerk	AS	0
0	0	Accounts Payable Clerk	BSF	0.2
0	0	Accounts Payable Clerk	ASF	0
0	0	Accounts Payable Clerk	M	0.2
0	0	Accounts Payable Clerk	AM	0
0	0	Accounts Payable Clerk	PC	0.1
0	0	Accounts Payable Clerk	S	0.2
0	0	Accounts Payable Clerk	AB	1
0	0	Accounts Payable Clerk	MK	0.1
0	0	Accounts Payable Clerk	OW	1
0	0	Accounts Payable Clerk	PW	0.2
0	0	Accounts Receivable Clerk	CL	0
0	0	Accounts Receivable Clerk	PL	0
0	0	Accounts Receivable Clerk	GFA	0.33
0	0	Accounts Receivable Clerk	SFA	0
0	0	Accounts Receivable Clerk	BE	0.2
0	0	Accounts Receivable Clerk	AE	0
0	0	Accounts Receivable Clerk	BS	0.2
0	0	Accounts Receivable Clerk	AS	0
0	0	Accounts Receivable Clerk	BSF	0.2
0	0	Accounts Receivable Clerk	ASF	0
0	0	Accounts Receivable Clerk	M	0.2
0	0	Accounts Receivable Clerk	AM	0
0	0	Accounts Receivable Clerk	PC	0.1
0	0	Accounts Receivable Clerk	S	0.2
0	0	Accounts Receivable Clerk	AB	1
0	0	Accounts Receivable Clerk	MK	0.1
0	0	Accounts Receivable Clerk	OW	1
0	0	Accounts Receivable Clerk	PW	0.2
0	0	Receptionist	CL	0
0	0	Receptionist	PL	0
0	0	Receptionist	GFA	0
0	0	Receptionist	SFA	0
0	0	Receptionist	BE	0
0	0	Receptionist	AE	0
0	0	Receptionist	BS	0
0	0	Receptionist	AS	0
0	0	Receptionist	BSF	0
0	0	Receptionist	ASF	0
0	0	Receptionist	M	0.1
0	0	Receptionist	AM	0
0	0	Receptionist	PC	0
0	0	Receptionist	S	0.1
0	0	Receptionist	AB	0.33
0	0	Receptionist	MK	0.25
0	0	Receptionist	OW	1
0	0	Receptionist	PW	0.2
0	0	Secretary	CL	0
0	0	Secretary	PL	0
0	0	Secretary	GFA	0
0	0	Secretary	SFA	0
0	0	Secretary	BE	0
0	0	Secretary	AE	0
0	0	Secretary	BS	0
0	0	Secretary	AS	0
0	0	Secretary	BSF	0
0	0	Secretary	ASF	0
0	0	Secretary	M	0.1
0	0	Secretary	AM	0
0	0	Secretary	PC	0.2
0	0	Secretary	S	0.1
0	0	Secretary	AB	0.5
0	0	Secretary	MK	0.2
0	0	Secretary	OW	1
0	0	Secretary	PW	0.2
0	0	Administrative Assistant	CL	0
0	0	Administrative Assistant	PL	0.2
0	0	Administrative Assistant	GFA	0.25
0	0	Administrative Assistant	SFA	0
0	0	Administrative Assistant	BE	0.25
0	0	Administrative Assistant	AE	0
0	0	Administrative Assistant	BS	0.25
0	0	Administrative Assistant	AS	0
0	0	Administrative Assistant	BSF	0.5
0	0	Administrative Assistant	ASF	0
0	0	Administrative Assistant	M	0.25
0	0	Administrative Assistant	AM	0.2
0	0	Administrative Assistant	PC	0.66
0	0	Administrative Assistant	S	0.5
0	0	Administrative Assistant	AB	0.5
0	0	Administrative Assistant	MK	0.2
0	0	Administrative Assistant	OW	1
0	0	Administrative Assistant	PW	0.2
0	0	Office Engineer	CL	0
0	0	Office Engineer	PL	0.25
0	0	Office Engineer	GFA	0.33
0	0	Office Engineer	SFA	0
0	0	Office Engineer	BE	0.33
0	0	Office Engineer	AE	0
0	0	Office Engineer	BS	0.33
0	0	Office Engineer	AS	0
0	0	Office Engineer	BSF	0.5
0	0	Office Engineer	ASF	0
0	0	Office Engineer	M	0.25
0	0	Office Engineer	AM	0.2
0	0	Office Engineer	PC	1
0	0	Office Engineer	S	0.5
0	0	Office Engineer	AB	0.5
0	0	Office Engineer	MK	0.2
0	0	Office Engineer	OW	1
0	0	Office Engineer	PW	0.2
0	0	Office Manager	CL	0
0	0	Office Manager	PL	0.1
0	0	Office Manager	GFA	0.25
0	0	Office Manager	SFA	0
0	0	Office Manager	BE	0.25
0	0	Office Manager	AE	0
0	0	Office Manager	BS	0.25
0	0	Office Manager	AS	0
0	0	Office Manager	BSF	0.33
0	0	Office Manager	ASF	0
0	0	Office Manager	M	1
0	0	Office Manager	AM	0.33
0	0	Office Manager	PC	0.5
0	0	Office Manager	S	0.5
0	0	Office Manager	AB	0.5
0	0	Office Manager	MK	0.2
0	0	Office Manager	OW	1
0	0	Office Manager	PW	0.2
0	0	Blue Print Clerk	CL	0
0	0	Blue Print Clerk	PL	0
0	0	Blue Print Clerk	GFA	0
0	0	Blue Print Clerk	SFA	0
0	0	Blue Print Clerk	BE	0
0	0	Blue Print Clerk	AE	0
0	0	Blue Print Clerk	BS	0
0	0	Blue Print Clerk	AS	0
0	0	Blue Print Clerk	BSF	0
0	0	Blue Print Clerk	ASF	0
0	0	Blue Print Clerk	M	0.1
0	0	Blue Print Clerk	AM	0
0	0	Blue Print Clerk	PC	0.2
0	0	Blue Print Clerk	S	0.1
0	0	Blue Print Clerk	AB	0.33
0	0	Blue Print Clerk	MK	0.2
0	0	Blue Print Clerk	OW	1
0	0	Blue Print Clerk	PW	0.2
0	0	Intern Student	CL	0
0	0	Intern Student	PL	0
0	0	Intern Student	GFA	0.2
0	0	Intern Student	SFA	0.1
0	0	Intern Student	BE	0.2
0	0	Intern Student	AE	0.1
0	0	Intern Student	BS	0.2
0	0	Intern Student	AS	0.1
0	0	Intern Student	BSF	0.2
0	0	Intern Student	ASF	0.1
0	0	Intern Student	M	0.25
0	0	Intern Student	AM	0.1
0	0	Intern Student	PC	0.33
0	0	Intern Student	S	0.2
0	0	Intern Student	AB	0.33
0	0	Intern Student	MK	0.2
0	0	Intern Student	OW	1
0	0	Intern Student	PW	0.2
0	0	Coop Student	CL	0
0	0	Coop Student	PL	0
0	0	Coop Student	GFA	0.2
0	0	Coop Student	SFA	0.1
0	0	Coop Student	BE	0.2
0	0	Coop Student	AE	0.1
0	0	Coop Student	BS	0.2
0	0	Coop Student	AS	0.1
0	0	Coop Student	BSF	0.2
0	0	Coop Student	ASF	0.1
0	0	Coop Student	M	0.25
0	0	Coop Student	AM	0.1
0	0	Coop Student	PC	0.33
0	0	Coop Student	S	0.2
0	0	Coop Student	AB	0.33
0	0	Coop Student	MK	0.2
0	0	Coop Student	OW	1
0	0	Coop Student	PW	0.2
0	0	Contracts Coordinator	CL	0
0	0	Contracts Coordinator	PL	0.2
0	0	Contracts Coordinator	GFA	0.2
0	0	Contracts Coordinator	SFA	0
0	0	Contracts Coordinator	BE	0.25
0	0	Contracts Coordinator	AE	0
0	0	Contracts Coordinator	BS	0.25
0	0	Contracts Coordinator	AS	0
0	0	Contracts Coordinator	BSF	0.33
0	0	Contracts Coordinator	ASF	0
0	0	Contracts Coordinator	M	0.25
0	0	Contracts Coordinator	AM	0.2
0	0	Contracts Coordinator	PC	0.5
0	0	Contracts Coordinator	S	0.25
0	0	Contracts Coordinator	AB	0.5
0	0	Contracts Coordinator	MK	0.2
0	0	Contracts Coordinator	OW	1
0	0	Contracts Coordinator	PW	0.2
0	0	Project Coordinator	CL	0
0	0	Project Coordinator	PL	0.2
0	0	Project Coordinator	GFA	0.25
0	0	Project Coordinator	SFA	0
0	0	Project Coordinator	BE	0.25
0	0	Project Coordinator	AE	0
0	0	Project Coordinator	BS	0.25
0	0	Project Coordinator	AS	0
0	0	Project Coordinator	BSF	0.5
0	0	Project Coordinator	ASF	0
0	0	Project Coordinator	M	0.25
0	0	Project Coordinator	AM	0.2
0	0	Project Coordinator	PC	0.66
0	0	Project Coordinator	S	0.5
0	0	Project Coordinator	AB	0.5
0	0	Project Coordinator	MK	0.25
0	0	Project Coordinator	OW	1
0	0	Project Coordinator	PW	0.2
0	0	Senior Project Coordinator	CL	0
0	0	Senior Project Coordinator	PL	0.5
0	0	Senior Project Coordinator	GFA	0.25
0	0	Senior Project Coordinator	SFA	0
0	0	Senior Project Coordinator	BE	0.25
0	0	Senior Project Coordinator	AE	0
0	0	Senior Project Coordinator	BS	0.25
0	0	Senior Project Coordinator	AS	0
0	0	Senior Project Coordinator	BSF	0.5
0	0	Senior Project Coordinator	ASF	0
0	0	Senior Project Coordinator	M	0.25
0	0	Senior Project Coordinator	AM	0.33
0	0	Senior Project Coordinator	PC	1
0	0	Senior Project Coordinator	S	0.5
0	0	Senior Project Coordinator	AB	0.5
0	0	Senior Project Coordinator	MK	0.2
0	0	Senior Project Coordinator	OW	1
0	0	Senior Project Coordinator	PW	0.2
0	0	Assistant Project Manager	CL	0
0	0	Assistant Project Manager	PL	0.25
0	0	Assistant Project Manager	GFA	0.2
0	0	Assistant Project Manager	SFA	0
0	0	Assistant Project Manager	BE	0.33
0	0	Assistant Project Manager	AE	0
0	0	Assistant Project Manager	BS	1
0	0	Assistant Project Manager	AS	0.33
0	0	Assistant Project Manager	BSF	0.5
0	0	Assistant Project Manager	ASF	0
0	0	Assistant Project Manager	M	0.5
0	0	Assistant Project Manager	AM	0.2
0	0	Assistant Project Manager	PC	1
0	0	Assistant Project Manager	S	0.25
0	0	Assistant Project Manager	AB	0.5
0	0	Assistant Project Manager	MK	0.25
0	0	Assistant Project Manager	OW	1
0	0	Assistant Project Manager	PW	0.2
0	0	Project Manager	CL	0.1
0	0	Project Manager	PL	0.33
0	0	Project Manager	GFA	0.33
0	0	Project Manager	SFA	0.1
0	0	Project Manager	BE	0.33
0	0	Project Manager	AE	0.1
0	0	Project Manager	BS	0.33
0	0	Project Manager	AS	0.5
0	0	Project Manager	BSF	1
0	0	Project Manager	ASF	0.25
0	0	Project Manager	M	1
0	0	Project Manager	AM	0.5
0	0	Project Manager	PC	1
0	0	Project Manager	S	0.2
0	0	Project Manager	AB	1
0	0	Project Manager	MK	0.25
0	0	Project Manager	OW	1
0	0	Project Manager	PW	0.2
0	0	Senior Project Manager	CL	0.2
0	0	Senior Project Manager	PL	0.5
0	0	Senior Project Manager	GFA	1
0	0	Senior Project Manager	SFA	0.2
0	0	Senior Project Manager	BE	1
0	0	Senior Project Manager	AE	0.2
0	0	Senior Project Manager	BS	1
0	0	Senior Project Manager	AS	1
0	0	Senior Project Manager	BSF	1
0	0	Senior Project Manager	ASF	0.5
0	0	Senior Project Manager	M	1
0	0	Senior Project Manager	AM	0.75
0	0	Senior Project Manager	PC	1
0	0	Senior Project Manager	S	0.5
0	0	Senior Project Manager	AB	1
0	0	Senior Project Manager	MK	0.25
0	0	Senior Project Manager	OW	1
0	0	Senior Project Manager	PW	0.2
0	0	Assistant Estimator	CL	0
0	0	Assistant Estimator	PL	0
0	0	Assistant Estimator	GFA	0.2
0	0	Assistant Estimator	SFA	0
0	0	Assistant Estimator	BE	1
0	0	Assistant Estimator	AE	0.33
0	0	Assistant Estimator	BS	0.5
0	0	Assistant Estimator	AS	0.2
0	0	Assistant Estimator	BSF	0.2
0	0	Assistant Estimator	ASF	0
0	0	Assistant Estimator	M	0.25
0	0	Assistant Estimator	AM	0
0	0	Assistant Estimator	PC	0.25
0	0	Assistant Estimator	S	0.2
0	0	Assistant Estimator	AB	0.5
0	0	Assistant Estimator	MK	0.1
0	0	Assistant Estimator	OW	1
0	0	Assistant Estimator	PW	0.2
0	0	Estimator	CL	0
0	0	Estimator	PL	0.2
0	0	Estimator	GFA	0.25
0	0	Estimator	SFA	0.1
0	0	Estimator	BE	1
0	0	Estimator	AE	0.5
0	0	Estimator	BS	1
0	0	Estimator	AS	0.25
0	0	Estimator	BSF	0.2
0	0	Estimator	ASF	0
0	0	Estimator	M	0.25
0	0	Estimator	AM	0
0	0	Estimator	PC	0.25
0	0	Estimator	S	0.2
0	0	Estimator	AB	0.5
0	0	Estimator	MK	0.1
0	0	Estimator	OW	1
0	0	Estimator	PW	0.2
0	0	Senior Estimator	CL	0
0	0	Senior Estimator	PL	0.33
0	0	Senior Estimator	GFA	0.33
0	0	Senior Estimator	SFA	0.125
0	0	Senior Estimator	BE	1
0	0	Senior Estimator	AE	1
0	0	Senior Estimator	BS	1
0	0	Senior Estimator	AS	0.33
0	0	Senior Estimator	BSF	0.2
0	0	Senior Estimator	ASF	0
0	0	Senior Estimator	M	0.25
0	0	Senior Estimator	AM	0.1
0	0	Senior Estimator	PC	0.25
0	0	Senior Estimator	S	0.2
0	0	Senior Estimator	AB	0.5
0	0	Senior Estimator	MK	0.1
0	0	Senior Estimator	OW	1
0	0	Senior Estimator	PW	0.2
0	0	Estimating Coordinator	CL	0
0	0	Estimating Coordinator	PL	0
0	0	Estimating Coordinator	GFA	0.2
0	0	Estimating Coordinator	SFA	0
0	0	Estimating Coordinator	BE	0.5
0	0	Estimating Coordinator	AE	0.1
0	0	Estimating Coordinator	BS	0.2
0	0	Estimating Coordinator	AS	0
0	0	Estimating Coordinator	BSF	0.2
0	0	Estimating Coordinator	ASF	0
0	0	Estimating Coordinator	M	0.2
0	0	Estimating Coordinator	AM	0
0	0	Estimating Coordinator	PC	0.25
0	0	Estimating Coordinator	S	0.2
0	0	Estimating Coordinator	AB	0.5
0	0	Estimating Coordinator	MK	0.1
0	0	Estimating Coordinator	OW	1
0	0	Estimating Coordinator	PW	0.2
0	0	Takeoff Person	CL	0
0	0	Takeoff Person	PL	0
0	0	Takeoff Person	GFA	0.2
0	0	Takeoff Person	SFA	0
0	0	Takeoff Person	BE	0.66
0	0	Takeoff Person	AE	0.1
0	0	Takeoff Person	BS	0.2
0	0	Takeoff Person	AS	0
0	0	Takeoff Person	BSF	0.2
0	0	Takeoff Person	ASF	0
0	0	Takeoff Person	M	0.2
0	0	Takeoff Person	AM	0
0	0	Takeoff Person	PC	0.25
0	0	Takeoff Person	S	0.2
0	0	Takeoff Person	AB	0.5
0	0	Takeoff Person	MK	0.2
0	0	Takeoff Person	OW	1
0	0	Takeoff Person	PW	0.2
0	0	Preconstruction Services Manager	CL	0.2
0	0	Preconstruction Services Manager	PL	1
0	0	Preconstruction Services Manager	GFA	0.5
0	0	Preconstruction Services Manager	SFA	0.2
0	0	Preconstruction Services Manager	BE	0.25
0	0	Preconstruction Services Manager	AE	0.1
0	0	Preconstruction Services Manager	BS	1
0	0	Preconstruction Services Manager	AS	1
0	0	Preconstruction Services Manager	BSF	1
0	0	Preconstruction Services Manager	ASF	0.5
0	0	Preconstruction Services Manager	M	1
0	0	Preconstruction Services Manager	AM	0.66
0	0	Preconstruction Services Manager	PC	1
0	0	Preconstruction Services Manager	S	0.5
0	0	Preconstruction Services Manager	AB	0.5
0	0	Preconstruction Services Manager	MK	1
0	0	Preconstruction Services Manager	OW	1
0	0	Preconstruction Services Manager	PW	0.2
0	0	Warehouse Manager	CL	0
0	0	Warehouse Manager	PL	0
0	0	Warehouse Manager	GFA	0
0	0	Warehouse Manager	SFA	0
0	0	Warehouse Manager	BE	0
0	0	Warehouse Manager	AE	0
0	0	Warehouse Manager	BS	0
0	0	Warehouse Manager	AS	0
0	0	Warehouse Manager	BSF	0.5
0	0	Warehouse Manager	ASF	0
0	0	Warehouse Manager	M	0
0	0	Warehouse Manager	AM	0
0	0	Warehouse Manager	PC	0
0	0	Warehouse Manager	S	0
0	0	Warehouse Manager	AB	0
0	0	Warehouse Manager	MK	0
0	0	Warehouse Manager	OW	0
0	0	Warehouse Manager	PW	1
0	0	Warehouse Teamster	CL	0
0	0	Warehouse Teamster	PL	0
0	0	Warehouse Teamster	GFA	0
0	0	Warehouse Teamster	SFA	0
0	0	Warehouse Teamster	BE	0
0	0	Warehouse Teamster	AE	0
0	0	Warehouse Teamster	BS	0.5
0	0	Warehouse Teamster	AS	0
0	0	Warehouse Teamster	BSF	0
0	0	Warehouse Teamster	ASF	0
0	0	Warehouse Teamster	M	0
0	0	Warehouse Teamster	AM	0
0	0	Warehouse Teamster	PC	0
0	0	Warehouse Teamster	S	0
0	0	Warehouse Teamster	AB	0
0	0	Warehouse Teamster	MK	0
0	0	Warehouse Teamster	OW	0
0	0	Warehouse Teamster	PW	1
0	0	Warehouse Assistant	CL	0
0	0	Warehouse Assistant	PL	0
0	0	Warehouse Assistant	GFA	0
0	0	Warehouse Assistant	SFA	0
0	0	Warehouse Assistant	BE	0
0	0	Warehouse Assistant	AE	0
0	0	Warehouse Assistant	BS	0
0	0	Warehouse Assistant	AS	0
0	0	Warehouse Assistant	BSF	0.5
0	0	Warehouse Assistant	ASF	0
0	0	Warehouse Assistant	M	0
0	0	Warehouse Assistant	AM	0
0	0	Warehouse Assistant	PC	0
0	0	Warehouse Assistant	S	0
0	0	Warehouse Assistant	AB	0
0	0	Warehouse Assistant	MK	0
0	0	Warehouse Assistant	OW	0
0	0	Warehouse Assistant	PW	1
0	0	Cabinet Shop Manager	CL	0
0	0	Cabinet Shop Manager	PL	0
0	0	Cabinet Shop Manager	GFA	0
0	0	Cabinet Shop Manager	SFA	0
0	0	Cabinet Shop Manager	BE	0
0	0	Cabinet Shop Manager	AE	0
0	0	Cabinet Shop Manager	BS	0
0	0	Cabinet Shop Manager	AS	0
0	0	Cabinet Shop Manager	BSF	0.5
0	0	Cabinet Shop Manager	ASF	0
0	0	Cabinet Shop Manager	M	0
0	0	Cabinet Shop Manager	AM	0
0	0	Cabinet Shop Manager	PC	0
0	0	Cabinet Shop Manager	S	0
0	0	Cabinet Shop Manager	AB	0
0	0	Cabinet Shop Manager	MK	0
0	0	Cabinet Shop Manager	OW	0
0	0	Cabinet Shop Manager	PW	0.5
0	0	Concrete Services Manager	CL	0
0	0	Concrete Services Manager	PL	0
0	0	Concrete Services Manager	GFA	0
0	0	Concrete Services Manager	SFA	0
0	0	Concrete Services Manager	BE	0
0	0	Concrete Services Manager	AE	0
0	0	Concrete Services Manager	BS	0
0	0	Concrete Services Manager	AS	0
0	0	Concrete Services Manager	BSF	0.5
0	0	Concrete Services Manager	ASF	0
0	0	Concrete Services Manager	M	0
0	0	Concrete Services Manager	AM	0
0	0	Concrete Services Manager	PC	0
0	0	Concrete Services Manager	S	0
0	0	Concrete Services Manager	AB	0
0	0	Concrete Services Manager	MK	0
0	0	Concrete Services Manager	OW	0
0	0	Concrete Services Manager	PW	0.5
0	0	Human Resources Manager	CL	0
0	0	Human Resources Manager	PL	0
0	0	Human Resources Manager	GFA	0
0	0	Human Resources Manager	SFA	0
0	0	Human Resources Manager	BE	0
0	0	Human Resources Manager	AE	0
0	0	Human Resources Manager	BS	0
0	0	Human Resources Manager	AS	0
0	0	Human Resources Manager	BSF	0.5
0	0	Human Resources Manager	ASF	0.2
0	0	Human Resources Manager	M	1
0	0	Human Resources Manager	AM	0
0	0	Human Resources Manager	PC	0
0	0	Human Resources Manager	S	0.5
0	0	Human Resources Manager	AB	0.2
0	0	Human Resources Manager	MK	0.2
0	0	Human Resources Manager	OW	1
0	0	Human Resources Manager	PW	0.2
0	0	Safety Manager	CL	0
0	0	Safety Manager	PL	0
0	0	Safety Manager	GFA	0
0	0	Safety Manager	SFA	0
0	0	Safety Manager	BE	0
0	0	Safety Manager	AE	0
0	0	Safety Manager	BS	0
0	0	Safety Manager	AS	0
0	0	Safety Manager	BSF	1
0	0	Safety Manager	ASF	1
0	0	Safety Manager	M	1
0	0	Safety Manager	AM	0
0	0	Safety Manager	PC	0
0	0	Safety Manager	S	0.33
0	0	Safety Manager	AB	0.5
0	0	Safety Manager	MK	0.2
0	0	Safety Manager	OW	1
0	0	Safety Manager	PW	0.2
0	0	Marketing Manager	CL	0
0	0	Marketing Manager	PL	0
0	0	Marketing Manager	GFA	0
0	0	Marketing Manager	SFA	0
0	0	Marketing Manager	BE	0
0	0	Marketing Manager	AE	0
0	0	Marketing Manager	BS	0
0	0	Marketing Manager	AS	0
0	0	Marketing Manager	BSF	0
0	0	Marketing Manager	ASF	0
0	0	Marketing Manager	M	1
0	0	Marketing Manager	AM	0
0	0	Marketing Manager	PC	0
0	0	Marketing Manager	S	0.33
0	0	Marketing Manager	AB	0.5
0	0	Marketing Manager	MK	1
0	0	Marketing Manager	OW	1
0	0	Marketing Manager	PW	0.2
0	0	Marketing Coordinator	CL	0
0	0	Marketing Coordinator	PL	0
0	0	Marketing Coordinator	GFA	0
0	0	Marketing Coordinator	SFA	0
0	0	Marketing Coordinator	BE	0
0	0	Marketing Coordinator	AE	0
0	0	Marketing Coordinator	BS	0
0	0	Marketing Coordinator	AS	0
0	0	Marketing Coordinator	BSF	0
0	0	Marketing Coordinator	ASF	0
0	0	Marketing Coordinator	M	1
0	0	Marketing Coordinator	AM	0
0	0	Marketing Coordinator	PC	0
0	0	Marketing Coordinator	S	0.33
0	0	Marketing Coordinator	AB	0.5
0	0	Marketing Coordinator	MK	0.5
0	0	Marketing Coordinator	OW	1
0	0	Marketing Coordinator	PW	0.2
0	0	Business Development Manager	CL	0.33
0	0	Business Development Manager	PL	0.2
0	0	Business Development Manager	GFA	0.2
0	0	Business Development Manager	SFA	0.1
0	0	Business Development Manager	BE	0.2
0	0	Business Development Manager	AE	0.1
0	0	Business Development Manager	BS	0.2
0	0	Business Development Manager	AS	0.1
0	0	Business Development Manager	BSF	0.2
0	0	Business Development Manager	ASF	0.1
0	0	Business Development Manager	M	1
0	0	Business Development Manager	AM	0.1
0	0	Business Development Manager	PC	0.2
0	0	Business Development Manager	S	0.5
0	0	Business Development Manager	AB	1.11
0	0	Business Development Manager	MK	0.66
0	0	Business Development Manager	OW	1
0	0	Business Development Manager	PW	0.2
\.


--
-- Dependencies: 281
-- Data for Name: mosp_cost; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mosp_cost (adminid, gameid, skill_code, price) FROM stdin;
0	0	CL	150
0	0	PL	150
0	0	GFA	150
0	0	SFA	150
0	0	BE	150
0	0	AE	150
0	0	BS	150
0	0	AS	150
0	0	BSF	150
0	0	ASF	150
0	0	M	150
0	0	AM	150
0	0	PC	150
0	0	S	150
0	0	AB	150
0	0	MK	150
0	0	OW	150
0	0	PW	150
\.


--
-- Dependencies: 282
-- Data for Name: mosp_generation_jrnl; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mosp_generation_jrnl (gameid, employee_title, period_id, skill_code, points) FROM stdin;
\.


--
-- Dependencies: 283
-- Data for Name: mosp_unfilled_jrnl; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mosp_unfilled_jrnl (gameid, companyid, period_id, skill_code, points) FROM stdin;
\.


--
-- Dependencies: 284
-- Data for Name: negotiated_job; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.negotiated_job (customerid, companyid, jobid, gameid, agreed_cost, agreed_percent_profit, state, customer_target_profit, customer_haggling) FROM stdin;
\.


--
-- Dependencies: 285
-- Data for Name: overtime; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.overtime (activity_type_id, gameid, jobid, overtime_days) FROM stdin;
\.


--
-- Dependencies: 286
-- Data for Name: period; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.period (periodid, gameid, temperature, rainfall, labor_availability, material_cost_index, number_of_new_jobs, number_of_new_heavy_jobs, workdays_first_month, workdays_second_month, raindays_first_month, raindays_second_month) FROM stdin;
\.


--
-- Dependencies: 287
-- Data for Name: ratios; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ratios (gameid, companyid, periodid, current_ratio, receivable_turns, payable_turns, cash_turns, cash_to_wip, earnings_to_interest, debt_to_equity, gross_profit, net_profit, return_on_equity, return_on_assets, costs_to_sales, ga_to_sales, op_expense_to_sales) FROM stdin;
\.


--
-- Dependencies: 288
-- Data for Name: report_ledger; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_ledger (companyid, periodid, reportid, purchase_period) FROM stdin;
\.


--
-- Dependencies: 289
-- Data for Name: retention; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.retention (retentionid, percent_retention, percent_of_job) FROM stdin;
1	0	0
2	5	50
3	5	100
4	10	50
5	10	100
\.


--
-- Dependencies: 290
-- Data for Name: schedule_estimated_methods; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.schedule_estimated_methods (gameid, jobid, activityid, methodid) FROM stdin;
\.


--
-- Dependencies: 291
-- Data for Name: update_policy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.update_policy (gameid, policy) FROM stdin;
\.


--
-- Dependencies: 229
-- Name: big_adminid_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--



--
-- Dependencies: 230
-- Name: big_bidid_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--



--
-- Dependencies: 231
-- Name: big_cccid_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--



--
-- Dependencies: 232
-- Name: big_companyid_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--



--
-- Dependencies: 233
-- Name: big_customerid_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--



--
-- Dependencies: 234
-- Name: big_gameid_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--



--
-- Dependencies: 235
-- Name: big_loanid_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--



--
-- Dependencies: 252
-- Name: equipment_to_period_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--



--
-- Name: activity pk_activity; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.activity
    ADD CONSTRAINT pk_activity PRIMARY KEY (activity_type_id, gameid, jobid, periodid);


--
-- Name: activity_parameters pk_activity_parameters; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.activity_parameters
    ADD CONSTRAINT pk_activity_parameters PRIMARY KEY (gameid, adminid, job_type_id, activity_type_id);


--
-- Name: admin pk_admin; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin
    ADD CONSTRAINT pk_admin PRIMARY KEY (adminid);


--
-- Name: appraisal_metrics pk_appraisal_metrics; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appraisal_metrics
    ADD CONSTRAINT pk_appraisal_metrics PRIMARY KEY (companyid, periodid);


--
-- Name: appraisal_metrics_updates pk_appraisal_metrics_updates; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.appraisal_metrics_updates
    ADD CONSTRAINT pk_appraisal_metrics_updates PRIMARY KEY (companyid);


--
-- Name: available_equipment pk_available_equipment; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.available_equipment
    ADD CONSTRAINT pk_available_equipment PRIMARY KEY (gameid, adminid, equipment_title);


--
-- Name: available_personnel pk_available_personnel; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.available_personnel
    ADD CONSTRAINT pk_available_personnel PRIMARY KEY (gameid, adminid, employee_title);


--
-- Name: balance_sheet pk_balance_sheet; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.balance_sheet
    ADD CONSTRAINT pk_balance_sheet PRIMARY KEY (gameid, companyid, periodid);


--
-- Name: bid pk_bid; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bid
    ADD CONSTRAINT pk_bid PRIMARY KEY (companyid, jobid, gameid);


--
-- Name: bid_method pk_bidmeth; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bid_method
    ADD CONSTRAINT pk_bidmeth PRIMARY KEY (companyid, jobid, bid_activity);


--
-- Name: billing_breakdown pk_billing_breakdown; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.billing_breakdown
    ADD CONSTRAINT pk_billing_breakdown PRIMARY KEY (gameid, companyid, periodid, jobid, activityid);


--
-- Name: billings pk_billings; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.billings
    ADD CONSTRAINT pk_billings PRIMARY KEY (gameid, companyid, periodid, jobid);


--
-- Name: ccc_job_type_preferences pk_ccc_job_type_preferences; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ccc_job_type_preferences
    ADD CONSTRAINT pk_ccc_job_type_preferences PRIMARY KEY (cccid, job_type_id);


--
-- Name: company pk_company; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company
    ADD CONSTRAINT pk_company PRIMARY KEY (companyid, gameid);


--
-- Name: company_personnel pk_company_personnel; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_personnel
    ADD CONSTRAINT pk_company_personnel PRIMARY KEY (gameid, companyid, periodid, employee_title);


--
-- Name: computer_controlled_contractors pk_computer_controlled_contractors; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.computer_controlled_contractors
    ADD CONSTRAINT pk_computer_controlled_contractors PRIMARY KEY (cccid);


--
-- Name: contract_reports pk_contract_reports; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.contract_reports
    ADD CONSTRAINT pk_contract_reports PRIMARY KEY (gameid, companyid, periodid, jobid);


--
-- Name: current_methods pk_current_methods; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.current_methods
    ADD CONSTRAINT pk_current_methods PRIMARY KEY (gameid, jobid, activity_type_id);


--
-- Name: dates_update_policy pk_dates_update_policy; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dates_update_policy
    ADD CONSTRAINT pk_dates_update_policy PRIMARY KEY (gameid, date);


--
-- Name: days_update_policy pk_days_update_policy; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.days_update_policy
    ADD CONSTRAINT pk_days_update_policy PRIMARY KEY (gameid, day);


--
-- Name: equip_network_dep pk_equip_network_dep; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equip_network_dep
    ADD CONSTRAINT pk_equip_network_dep PRIMARY KEY (gameid, adminid, job_type_id, activity_type_id);


--
-- Name: financial_report pk_financial_report; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.financial_report
    ADD CONSTRAINT pk_financial_report PRIMARY KEY (gameid, companyid, periodid);


--
-- Name: game pk_game; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game
    ADD CONSTRAINT pk_game PRIMARY KEY (gameid);


--
-- Name: game_params_ccc pk_game_params_ccc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_ccc
    ADD CONSTRAINT pk_game_params_ccc PRIMARY KEY (gameid, adminid);


--
-- Name: game_params_job_size pk_game_params_job_size; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_job_size
    ADD CONSTRAINT pk_game_params_job_size PRIMARY KEY (gameid, job_type_id, adminid);


--
-- Name: game_params_jobs_per_period pk_game_params_jobs_per_period; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_jobs_per_period
    ADD CONSTRAINT pk_game_params_jobs_per_period PRIMARY KEY (gameid, periodid, adminid);


--
-- Name: game_params_labor_avail pk_game_params_labor_avail; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_labor_avail
    ADD CONSTRAINT pk_game_params_labor_avail PRIMARY KEY (gameid, periodid, adminid);


--
-- Name: game_params_liquidated_damages pk_game_params_liquidated_damages; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_liquidated_damages
    ADD CONSTRAINT pk_game_params_liquidated_damages PRIMARY KEY (gameid, adminid);


--
-- Name: game_params_material_cost_idx pk_game_params_matl_cost_idx; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_material_cost_idx
    ADD CONSTRAINT pk_game_params_matl_cost_idx PRIMARY KEY (gameid, periodid, adminid);


--
-- Name: game_params_misc pk_game_params_misc; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_misc
    ADD CONSTRAINT pk_game_params_misc PRIMARY KEY (gameid, adminid);


--
-- Name: game_params_negotiation pk_game_params_negotiation; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_negotiation
    ADD CONSTRAINT pk_game_params_negotiation PRIMARY KEY (gameid, adminid);


--
-- Name: game_params_overhead pk_game_params_overhead; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_overhead
    ADD CONSTRAINT pk_game_params_overhead PRIMARY KEY (gameid, adminid);


--
-- Name: game_params_percent_takeoff pk_game_params_percent_takeoff; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_percent_takeoff
    ADD CONSTRAINT pk_game_params_percent_takeoff PRIMARY KEY (gameid, job_type_id, activity_type_id, adminid);


--
-- Name: game_params_rainfall pk_game_params_rainfall; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_rainfall
    ADD CONSTRAINT pk_game_params_rainfall PRIMARY KEY (gameid, periodid, adminid);


--
-- Name: game_params_report_costs pk_game_params_report_costs; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_report_costs
    ADD CONSTRAINT pk_game_params_report_costs PRIMARY KEY (gameid, adminid);


--
-- Name: game_params_temperature pk_game_params_temperature; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.game_params_temperature
    ADD CONSTRAINT pk_game_params_temperature PRIMARY KEY (gameid, periodid, adminid);


--
-- Name: grading_template pk_grading_template; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grading_template
    ADD CONSTRAINT pk_grading_template PRIMARY KEY (adminid, gameid);


--
-- Name: interval_update_policy pk_interval_update_policy; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.interval_update_policy
    ADD CONSTRAINT pk_interval_update_policy PRIMARY KEY (gameid);


--
-- Name: job pk_job; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job
    ADD CONSTRAINT pk_job PRIMARY KEY (jobid, gameid);


--
-- Name: job_financial_info pk_job_financial_info; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_financial_info
    ADD CONSTRAINT pk_job_financial_info PRIMARY KEY (gameid, companyid, periodid, jobid);


--
-- Name: job_type_req_mos pk_job_type_req_mos; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_type_req_mos
    ADD CONSTRAINT pk_job_type_req_mos PRIMARY KEY (adminid, gameid, job_type_id, skill_code);


--
-- Name: loan pk_loan; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan
    ADD CONSTRAINT pk_loan PRIMARY KEY (loanid, gameid, companyid, periodid);


--
-- Name: main_office_skills pk_main_office_skills; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.main_office_skills
    ADD CONSTRAINT pk_main_office_skills PRIMARY KEY (gameid, adminid, skill_code);


--
-- Name: method pk_method; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.method
    ADD CONSTRAINT pk_method PRIMARY KEY (gameid, adminid, activity_type_id, methodid);


--
-- Name: mos_point_conversion pk_mos_point_conversion; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mos_point_conversion
    ADD CONSTRAINT pk_mos_point_conversion PRIMARY KEY (gameid, adminid, employee_title, skill_code);


--
-- Name: mosp_cost pk_mosp_cost; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mosp_cost
    ADD CONSTRAINT pk_mosp_cost PRIMARY KEY (adminid, gameid, skill_code);


--
-- Name: mosp_generation_jrnl pk_mosp_generation_jrnl; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mosp_generation_jrnl
    ADD CONSTRAINT pk_mosp_generation_jrnl PRIMARY KEY (gameid, employee_title, period_id, skill_code);


--
-- Name: mosp_unfilled_jrnl pk_mosp_unfilled_jrnl; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mosp_unfilled_jrnl
    ADD CONSTRAINT pk_mosp_unfilled_jrnl PRIMARY KEY (gameid, companyid, period_id, skill_code);


--
-- Name: overtime pk_overtime; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.overtime
    ADD CONSTRAINT pk_overtime PRIMARY KEY (activity_type_id, gameid, jobid);


--
-- Name: period pk_period; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.period
    ADD CONSTRAINT pk_period PRIMARY KEY (periodid, gameid);


--
-- Name: report_ledger pk_report_ledger; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_ledger
    ADD CONSTRAINT pk_report_ledger PRIMARY KEY (companyid, periodid, reportid);


--
-- Name: retention pk_retention; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.retention
    ADD CONSTRAINT pk_retention PRIMARY KEY (retentionid);


--
-- Name: update_policy pk_update_policy; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.update_policy
    ADD CONSTRAINT pk_update_policy PRIMARY KEY (gameid);


--
-- Name: available_equipment fk_available_equipment_adminid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.available_equipment
    ADD CONSTRAINT fk_available_equipment_adminid FOREIGN KEY (adminid) REFERENCES public.admin(adminid) ON DELETE CASCADE;


--
-- Name: available_equipment fk_available_equipment_gameid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.available_equipment
    ADD CONSTRAINT fk_available_equipment_gameid FOREIGN KEY (gameid) REFERENCES public.game(gameid) ON DELETE CASCADE;


--
-- Name: available_personnel fk_available_personnel_adminid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.available_personnel
    ADD CONSTRAINT fk_available_personnel_adminid FOREIGN KEY (adminid) REFERENCES public.admin(adminid) ON DELETE CASCADE;


--
-- Name: available_personnel fk_available_personnel_gameid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.available_personnel
    ADD CONSTRAINT fk_available_personnel_gameid FOREIGN KEY (gameid) REFERENCES public.game(gameid) ON DELETE CASCADE;


--
-- Name: company_personnel fk_company_personnel_companyid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_personnel
    ADD CONSTRAINT fk_company_personnel_companyid FOREIGN KEY (gameid, companyid) REFERENCES public.company(gameid, companyid) ON DELETE CASCADE;


--
-- Name: dates_update_policy fk_dates_update_policy_gameid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dates_update_policy
    ADD CONSTRAINT fk_dates_update_policy_gameid FOREIGN KEY (gameid) REFERENCES public.game(gameid) ON DELETE CASCADE;


--
-- Name: days_update_policy fk_days_update_policy_gameid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.days_update_policy
    ADD CONSTRAINT fk_days_update_policy_gameid FOREIGN KEY (gameid) REFERENCES public.game(gameid) ON DELETE CASCADE;


--
-- Name: grading_template fk_grading_template_admin; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grading_template
    ADD CONSTRAINT fk_grading_template_admin FOREIGN KEY (adminid) REFERENCES public.admin(adminid) ON DELETE CASCADE;


--
-- Name: grading_template fk_grading_template_game; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grading_template
    ADD CONSTRAINT fk_grading_template_game FOREIGN KEY (gameid) REFERENCES public.game(gameid) ON DELETE CASCADE;


--
-- Name: interval_update_policy fk_interval_update_policy_gameid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.interval_update_policy
    ADD CONSTRAINT fk_interval_update_policy_gameid FOREIGN KEY (gameid) REFERENCES public.game(gameid) ON DELETE CASCADE;


--
-- Name: job_type_req_mos fk_job_type_req_mos_skill_code; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_type_req_mos
    ADD CONSTRAINT fk_job_type_req_mos_skill_code FOREIGN KEY (adminid, gameid, skill_code) REFERENCES public.main_office_skills(adminid, gameid, skill_code) ON DELETE CASCADE;


--
-- Name: main_office_skills fk_maik_office_skills_adminid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.main_office_skills
    ADD CONSTRAINT fk_maik_office_skills_adminid FOREIGN KEY (adminid) REFERENCES public.admin(adminid) ON DELETE CASCADE;


--
-- Name: main_office_skills fk_main_office_skills_gameid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.main_office_skills
    ADD CONSTRAINT fk_main_office_skills_gameid FOREIGN KEY (gameid) REFERENCES public.game(gameid) ON DELETE CASCADE;


--
-- Name: mos_point_conversion fk_mos_point_conversion_employee; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mos_point_conversion
    ADD CONSTRAINT fk_mos_point_conversion_employee FOREIGN KEY (gameid, adminid, employee_title) REFERENCES public.available_personnel(gameid, adminid, employee_title) ON DELETE CASCADE;


--
-- Name: mos_point_conversion fk_mos_point_conversion_skill; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mos_point_conversion
    ADD CONSTRAINT fk_mos_point_conversion_skill FOREIGN KEY (gameid, adminid, skill_code) REFERENCES public.main_office_skills(gameid, adminid, skill_code) ON DELETE CASCADE;


--
-- Name: mosp_cost fk_mosp_cost_skill_code; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mosp_cost
    ADD CONSTRAINT fk_mosp_cost_skill_code FOREIGN KEY (adminid, gameid, skill_code) REFERENCES public.main_office_skills(adminid, gameid, skill_code) ON DELETE CASCADE;


--
-- Name: mosp_generation_jrnl fk_mosp_generation_jrnl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mosp_generation_jrnl
    ADD CONSTRAINT fk_mosp_generation_jrnl FOREIGN KEY (gameid) REFERENCES public.game(gameid) ON DELETE CASCADE;


--
-- Name: mosp_unfilled_jrnl fk_mosp_unfilled_jrnl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mosp_unfilled_jrnl
    ADD CONSTRAINT fk_mosp_unfilled_jrnl FOREIGN KEY (gameid, companyid) REFERENCES public.company(gameid, companyid) ON DELETE CASCADE;


--
-- Name: update_policy fk_update_policy_gameid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.update_policy
    ADD CONSTRAINT fk_update_policy_gameid FOREIGN KEY (gameid) REFERENCES public.game(gameid) ON DELETE CASCADE;


-- Completed on 2026-02-18 22:46:36

--
-- PostgreSQL database dump complete
--



