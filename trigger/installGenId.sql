alter table service_area drop index UK_rwqbc3gsnpxt56pmfpnvhn3qe;

CREATE INDEX id_service_area_area_code ON service_area (area_code) USING BTREE;

DROP TRIGGER service_area_bi;

DELIMITER //

CREATE TRIGGER service_area_bi
BEFORE INSERT ON service_area FOR EACH ROW
BEGIN
    DECLARE iAreaCodeMAX INTEGER;
    SELECT CAST(MAX(area_code) AS INT) FROM service_area where organization_id = NEW.organization_id INTO iAreaCodeMAX;
    IF (iAreaCodeMAX IS NULL) THEN SET iAreaCodeMAX := 0; END IF;
    SET NEW.area_code = LPAD(iAreaCodeMAX + 1,5,'0');
END; //

DELIMITER ;



DROP TRIGGER site_equipment_bi;

DELIMITER //

CREATE TRIGGER site_equipment_bi
BEFORE INSERT ON site_equipment FOR EACH ROW
BEGIN
    DECLARE vAreaCode VARCHAR(10);
    DECLARE vItemType VARCHAR(10);
    DECLARE iEquipmentIdMAX INTEGER;
    
    SELECT CONCAT(o.code,sa.area_code) FROM organization o, service_area sa, site s, site_circuit sc 
    WHERE o.id = sa.organization_id AND sa.id = s.service_area_id AND s.id = sc.site_id AND sc.id = NEW.site_circuit_id INTO vAreaCode;
    
    SELECT it.code FROM item_type it, item_model im, item i, service_area_equipment sae 
    WHERE it.id = im.item_type_id AND im.id = i.model_id AND i.id = sae.item_id AND sae.id = NEW.service_area_equipment_id INTO vItemType;
    
    SELECT CAST(RIGHT(MAX(equipment_id),4) AS INT) FROM site_equipment WHERE equipment_id like CONCAT(vAreaCode,'-',vItemtype,'%') INTO iEquipmentIdMAX;

    IF (iEquipmentIdMAX IS NULL) THEN SET iEquipmentIdMAX := 0; END IF;
    
    SET NEW.equipment_id = CONCAT(vAreaCode,'-',vItemtype,LPAD(iEquipmentIdMAX + 1,4,'0'));
END; //

DELIMITER ;

