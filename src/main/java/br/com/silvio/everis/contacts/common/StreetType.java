package br.com.silvio.everis.contacts.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StreetType {
	STREET("Rua"), AVENUE("Avenida"), ROAD("Estrada"),
	
	A("Área"),
	AC("Acesso"),
	ACA("Acampamento"),
	ACL("Acesso Local"),
	AD("Adro"),
	AE("Área Especial"),
	AER("Aeroporto"),
	AL("Alameda"),
	AMD("Avenida Marginal Direita"),
	AME("Avenida Marginal Esquerda"),
	AN("Anel Viário"),
	ANT("Antiga Estrada"),
	ART("Artéria"),
	AT("Alto"),
	ATL("Atalho"),
	A_V("Área Verde"),
	AV("Avenida"),
	AVC("Avenida Contorno"),
	AVM("Avenida Marginal"),
	AVV("Avenida Velha"),
	BAL("Balneário"),
	BC("Beco"),
	BCO("Buraco"),
	BEL("Belvedere"),
	BL("Bloco"),
	BLO("Balão"),
	BLS("Blocos"),
	BLV("Bulevar"),
	BSQ("Bosque"),
	BVD("Boulevard"),
	BX("Baixa"),
	C("Cais"),
	CAL("Calçada"),
	CAM("Caminho"),
	CAN("Canal"),
	CH("Chácara"),
	CHA("Chapadão"),
	CIC("Ciclovia"),
	CIR("Circular"),
	CJ("Conjunto"),
	CJM("Conjunto Mutirão"),
	CMP("Complexo Viário"),
	COL("Colônia"),
	COM("Comunidade"),
	CON("Condomínio"),
	COR("Corredor"),
	CPO("Campo"),
	CRG("Córrego"),
	CTN("Contorno"),
	DSC("Descida"),
	DSV("Desvio"),
	DT("Distrito"),
	EB("Entre Bloco"),
//	EIM	Estrada Intermunicipal
//	ENS	Enseada
//	ENT	Entrada Particular
//	EQ	Entre Quadra
//	ESC	Escada
//	ESD	Escadaria
//	ESE	Estrada Estadual
//	ESI	Estrada Vicinal
//	ESL	Estrada de Ligação
//	ESM	Estrada Municipal
//	ESP	Esplanada
//	ESS	Estrada de Servidão
//	EST	Estrada
//	ESV	Estrada Velha
//	ETA	Estrada Antiga
//	ETC	Estação
//	ETD	Estádio
//	ETN	Estância
//	ETP	Estrada Particular
//	ETT	Estacionamento
//	EVA	Evangélica
//	EVD	Elevada
//	EX	Eixo Industrial
//	FAV	Favela
//	FAZ	Fazenda
//	FER	Ferrovia
//	FNT	Fonte
//	FRA	Feira
//	FTE	Forte
//	GAL	Galeria
//	GJA	Granja
//	HAB	Núcleo Habitacional
//	IA	Ilha
//	IND	Indeterminado
//	IOA	Ilhota
//	JD	Jardim
//	JDE	Jardinete
//	LD	Ladeira
//	LGA	Lagoa
//	LGO	Lago
//	LOT	Loteamento
//	LRG	Largo
//	LT	Lote
//	MER	Mercado
//	MNA	Marina
//	MOD	Modulo
//	MRG	Projeção
//	MRO	Morro
//	MTE	Monte
//	NUC	Núcleo
//	NUR	Núcleo Rural
//	OUT	Outeiro
//	PAR	Paralela
//	PAS	Passeio
//	PAT	Pátio
//	PC	Praça
//	PCE	Praça de Esportes
//	PDA	Parada
//	PDO	Paradouro
//	PNT	Ponta
//	PR	Praia
//	PRL	Prolongamento
//	PRM	Parque Municipal
//	PRQ	Parque
//	PRR	Parque Residencial
//	PSA	Passarela
//	PSG	Passagem
//	PSP	Passagem de Pedestre
//	PSS	Passagem Subterrânea
//	PTE	Ponte
//	PTO	Porto
//	Q	Quadra
//	QTA	Quinta
//	QTS	Quintas
//	R	Rua
//	R I	Rua Integração
//	R L	Rua de Ligação
//	R P	Rua Particular
//	R V	Rua Velha
//	RAM	Ramal
//	RCR	Recreio
//	REC	Recanto
//	RER	Retiro
//	RES	Residencial
//	RET	Reta
//	RLA	Ruela
//	RMP	Rampa
//	ROA	Rodo Anel
//	ROD	Rodovia
//	ROT	Rotula
//	RPE	Rua de Pedestre
//	RPR	Margem
//	RTN	Retorno
//	RTT	Rotatória
//	SEG	Segunda Avenida
//	SIT	Sitio
//	SRV	Servidão
//	ST	Setor
//	SUB	Subida
//	TCH	Trincheira
//	TER	Terminal
//	TR	Trecho
//	TRV	Trevo
//	TUN	Túnel
//	TV	Travessa
//	TVP	Travessa Particular
//	TVV	Travessa Velha
//	UNI	Unidade
//	V	Via
//	V C	Via Coletora
//	V L	Via Local
//	VAC	Via de Acesso
//	VAL	Vala
//	VCO	Via Costeira
//	VD	Viaduto
//	V-E	Via Expressa
//	VER	Vereda
//	VEV	Via Elevado
//	VL	Vila
//	VLA	Viela
//	VLE	Vale
//	VLT	Via Litorânea
//	VPE	Via de Pedestre
//	VRT	Variante
	
	ZIG("Zigue-Zague");
	
	private String description;
	
	StreetType(String description) {
		this.description = description;
	}

	@JsonValue
	public String getDescription() {
		return this.description;
	}
	
	@Override
	public String toString() {
		return this.getDescription();
	}
}
