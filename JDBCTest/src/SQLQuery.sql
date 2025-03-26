SELECT * FROM (
SELECT
    companycodes.name_col "companyNo",
    substr(event.groupnumber_txn,4,5) "groupNo",
     eventtype.id_col "eventType",
    event.idnumber_txn "eventId",
        event.bpmtaskid_txn "taskId",
    event.groupstatus_txn "groupStatus",
    event.bpmstatus_txn bpmStatus,
    reason.id_col bpmReason,
    team.name_col  "team",
    specialist.id_col "specialist",
    specialist.name_col "specialistName",
    manager.id_col "manager",
    manager.name_col "managerName",
   round(event.amountdue_fact,2) "amountDue",
    round(event.amountreceived_fact,2) "amountReceived",
  event.state_txn,
to_char(event.datedue_date,'yyyy-MM-dd') datedue_date,
to_char(event.daterecd_date,'yyyy-MM-dd') daterecd_date,
to_char(event.bpmreceiveddate_date,'MON DD YYYY HH12:MIAM') bpmRecdDate,
    event.taskage_fact "age",
    to_char(event.ctrl_insert_dt,'MON DD YYYY HH12:MIAM') dateinserted,
    ROW_NUMBER() OVER (partition by event.id_txn order by event.ctrl_insert_dt DESC) r
FROM
    WEBM9_AEN_BPM.bam_fact_invoice_v1 event,
    WEBM9_AEN_BPM.bam_dim_company_v1 companyCodes,
    WEBM9_AEN_BPM.bam_dim_reconev_v1 eventType,
    WEBM9_AEN_BPM.bam_dim_reconte_v1 team,
    WEBM9_AEN_BPM.bam_dim_aflacem_v1 specialist,
    WEBM9_AEN_BPM.bam_dim_aflacma_v1 manager,
    WEBM9_AEN_BPM.bam_dim_reconbp_v1 reason
    where
    event.ctrl_insert_dt > sysdate -7 and
    event.reconeventype_dim  =eventtype.bam_dim_reconev_v1_id and
    event.companynumber_dim =companycodes.bam_dim_company_v1_id and
     event.team_dim  =team.bam_dim_reconte_v1_id and
    event.specialist_dim =specialist.bam_dim_aflacem_v1_id and
    event.manager_dim =manager.bam_dim_aflacma_v1_id   and
    event.bpmreason_dim = reason.bam_dim_reconbp_v1_id
    )
WHERE r = 1
