/** Quantum element crystal card: faceted gem in element color + rarity/source/usage lines (Starships & Elements §8). */
export interface ElementCardProps {
  name?: string;
  element?: 'quantium'|'voidium'|'neutronite'|'singularium'|'phasium'|'entropium'|'aeon';
  rarity?: string;
  source?: string;
  usage?: string;
  description?: string;
  width?: number | string;
  style?: React.CSSProperties;
}
export declare function ElementCard(props: ElementCardProps): JSX.Element;
/** Quantum-field hazard level badge: rotated-diamond glyph + level label (§10). */
export interface HazardBadgeProps {
  level?: 'safe'|'caution'|'danger'|'extreme'|'unknown';
  note?: string;
  size?: number;
  style?: React.CSSProperties;
}
export declare function HazardBadge(props: HazardBadgeProps): JSX.Element;
