<?xml version="1.0" encoding="UTF-8"?>
<root><bloc id="0" label="BlocStart" position="1"><bloc id="3" label="BlocInitialisationComp" position="0"><parametre nom="idComposant" type="int" valeur="0"/></bloc></bloc><bloc id="1" label="BlocUpdate" position="2"><bloc id="5" label="BlocAllumerPin" position="1"><parametre nom="idComposant" type="int" valeur="0"/><parametre nom="etatPin" type="etatPin" valeur="HAUT"/></bloc><bloc id="6" label="BlocAttendre" position="2"><parametre nom="delai" type="int" valeur="500"/></bloc><bloc id="7" label="BlocAllumerPin" position="3"><parametre nom="idComposant" type="int" valeur="0"/><parametre nom="etatPin" type="etatPin" valeur="BAS"/></bloc><bloc id="8" label="BlocAttendre" position="4"><parametre nom="delai" type="int" valeur="500"/></bloc>
<bloc id="9" label="BlocConditions" position="5">
<parametre nom="param1" type="variable" valeur="0"/>
<parametre nom="param2" type="int" valeur="5"/></bloc>
</bloc>
<bloc id="2" label="BlocInit" position="0"><bloc id="4" label="BlocInitVariable" position="1">
    <parametre nom="idVariable" type="int" valeur="0"/>
</bloc></bloc>
<composant id="0" label="led"><slot couleur="[r=255,g=255,b=0]" pin="3" type="Digital"/></composant><variable id="0" nom="etat" type="int" valeur="0"/></root>