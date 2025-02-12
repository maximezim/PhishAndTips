import type { User } from '$types/users';
  
  export interface ParsedResult {
    Data: string;
    Type: string;
  }

  export interface ParsedData {
    count: number;
    results: ParsedResult[];
  }

  export interface ExportResult {
    data: string;
    module: string;
    last_seen: string;
    scan_name: string;
    event_type: string;
    scan_target: string;
    source_data: string;
    false_positive: number;
  }

  export interface ScanResult {
    parsed_data: ParsedData;
    export_result: ExportResult[];
  }

  export interface MyScan {
    target: string;
    id: string;
    modules: string;
    status: string;
    result: string; 
    spiderfootScanId: string;
    createdAt: string;
    updatedAt: string;
  }

  export interface UserScan {
    target: User;
    scan: MyScan | null;
    groupedResults: Map<string, ParsedResult[]> | null;
    score: number;
    scoreColor: string;
  }

  export const typeTranslations = new Map<string, string>([
    ["PGP_KEY", "Clé PGP"],
    ["MALICIOUS_EMAILADDR", "Adresse email malveillante"],
    ["USERNAME", "Nom d'utilisateur"],
    ["ROOT", "Adresse email principale"],
    ["EMAILADDR", "Adresse email"],
    ["EMAILADDR_COMPROMISED", "Adresse email compromise"],
    ["IP_ADDRESS", "Adresse IP"],
    ["DOMAIN_NAME", "Nom de domaine"],
    ["PHONE_NUMBER", "Numéro de téléphone"],
    ["SOCIAL_MEDIA", "Compte de réseau social"],
    ["LEAKED_CREDENTIAL", "Identifiant compromis"],
    ["HASH", "Hash (empreinte numérique)"],
    ["URL", "URL (adresse web)"],
    ["HOSTNAME", "Nom d'hôte"],
    ["SSL_CERTIFICATE", "Certificat SSL"],
    ["WHOIS", "Informations WHOIS"],
    ["DNS_RECORD", "Enregistrement DNS"],
    ["GEOLOCATION", "Géolocalisation"],
    ["COMPANY_NAME", "Nom de l'entreprise"],
    ["BREACH", "Violation de données"],
    ["VULNERABILITY", "Vulnérabilité"],
    ["AFFILIATE", "Affilié"],
    ["AFFILIATION", "Affiliation"],
    ["SUBDOMAIN", "Sous-domaine"],
    ["NETBLOCK", "Bloc réseau"],
    ["ASN", "Numéro de système autonome (ASN)"],
    ["PORT", "Port"],
    ["SERVICE", "Service"],
    ["TECHNOLOGY", "Technologie"],
    ["EMAIL_DOMAIN", "Domaine de l'email"],
    ["PASSWORD", "Mot de passe"],
    ["CVE", "Identifiant de vulnérabilité (CVE)"],
    ["CERTIFICATE", "Certificat"],
    ["REGISTRAR", "Bureau d'enregistrement"],
    ["REGISTRANT", "Enregistrant"],
    ["ORG", "Organisation"],
    ["ADDRESS", "Adresse physique"],
    ["PERSON", "Personne"],
    ["BANNER", "Bannière de service"],
    ["SPF_RECORD", "Enregistrement SPF"],
    ["DMARC_RECORD", "Enregistrement DMARC"],
    ["MX_RECORD", "Enregistrement MX"],
    ["TXT_RECORD", "Enregistrement TXT"],
    ["A_RECORD", "Enregistrement A"],
    ["AAAA_RECORD", "Enregistrement AAAA"],
    ["CNAME_RECORD", "Enregistrement CNAME"],
    ["PTR_RECORD", "Enregistrement PTR"],
    ["NS_RECORD", "Enregistrement NS"],
    ["SOA_RECORD", "Enregistrement SOA"],
    ["SRV_RECORD", "Enregistrement SRV"]
  ]);