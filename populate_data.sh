#!/bin/bash
GATEWAY="http://localhost:8080"

echo "=========================================="
echo "    POPULATION DE DONNÉES DE TEST"
echo "=========================================="

# 1. Création de Pompes
echo "[+] Création des Pompes..."

# Pompe Nord (Standard)
P1=$(curl -s -X POST $GATEWAY/energy-service/api/pompes \
  -H "Content-Type: application/json" \
  -d '{"reference":"POMPE-NORD-01", "puissance":1500, "etat":"ACTIVE", "dateInstallation":"2023-01-15"}' | jq -r '.id')

# Pompe Sud (Puissante)
P2=$(curl -s -X POST $GATEWAY/energy-service/api/pompes \
  -H "Content-Type: application/json" \
  -d '{"reference":"POMPE-SUD-02", "puissance":2200, "etat":"ACTIVE", "dateInstallation":"2023-02-20"}' | jq -r '.id')

# Pompe Est (Maintenance)
P3=$(curl -s -X POST $GATEWAY/energy-service/api/pompes \
  -H "Content-Type: application/json" \
  -d '{"reference":"POMPE-EST-03", "puissance":1000, "etat":"MAINTENANCE", "dateInstallation":"2022-11-10"}' | jq -r '.id')

# Pompe Ouest (Très Puissante)
P4=$(curl -s -X POST $GATEWAY/energy-service/api/pompes \
  -H "Content-Type: application/json" \
  -d '{"reference":"POMPE-OUEST-04", "puissance":3000, "etat":"ACTIVE", "dateInstallation":"2024-01-05"}' | jq -r '.id')

echo "    > Pompes créées avec IDs: $P1, $P2, $P3, $P4"

# 2. Création de Réservoirs
echo "[+] Création des Réservoirs..."

curl -s -X POST $GATEWAY/water-service/api/reservoirs \
  -H "Content-Type: application/json" \
  -d '{"nom":"Grand Bassin Nord", "capaciteTotale":50000, "volumeActuel":45000, "localisation":"Zone Nord"}' > /dev/null

curl -s -X POST $GATEWAY/water-service/api/reservoirs \
  -H "Content-Type: application/json" \
  -d '{"nom":"Citerne Principale", "capaciteTotale":25000, "volumeActuel":12000, "localisation":"Ferme Centrale"}' > /dev/null

curl -s -X POST $GATEWAY/water-service/api/reservoirs \
  -H "Content-Type: application/json" \
  -d '{"nom":"Réserve Secours", "capaciteTotale":10000, "volumeActuel":9500, "localisation":"Hangars"}' > /dev/null

echo "    > Réservoirs créés."

# 3. Création de Consommations (avec Alertes)
echo "[+] Création des Consommations..."

# Consommation Normale (Pompe 1)
curl -s -X POST $GATEWAY/energy-service/api/consommations \
  -H "Content-Type: application/json" \
  -d "{\"pompeId\":$P1, \"energieUtilisee\":1200, \"duree\":2, \"dateMesure\":\"2024-01-10T10:00:00\"}" > /dev/null

# Consommation Limite (Pompe 1)
curl -s -X POST $GATEWAY/energy-service/api/consommations \
  -H "Content-Type: application/json" \
  -d "{\"pompeId\":$P1, \"energieUtilisee\":1800, \"duree\":3, \"dateMesure\":\"2024-01-11T11:00:00\"}" > /dev/null

# ALERTE: Surconsommation (Pompe 2, >2000)
echo "    > Simulation Surconsommation Pompe 2..."
curl -s -X POST $GATEWAY/energy-service/api/consommations \
  -H "Content-Type: application/json" \
  -d "{\"pompeId\":$P2, \"energieUtilisee\":2300, \"duree\":3, \"dateMesure\":\"2024-01-11T14:30:00\"}" > /dev/null

# GROSSE ALERTE: Surconsommation (Pompe 4, >2000)
echo "    > Simulation Surconsommation Pompe 4..."
curl -s -X POST $GATEWAY/energy-service/api/consommations \
  -H "Content-Type: application/json" \
  -d "{\"pompeId\":$P4, \"energieUtilisee\":5000, \"duree\":5, \"dateMesure\":\"2024-01-12T09:15:00\"}" > /dev/null

echo "=========================================="
echo "    ✅ DONNÉES INSÉRÉES AVEC SUCCÈS"
echo "=========================================="
