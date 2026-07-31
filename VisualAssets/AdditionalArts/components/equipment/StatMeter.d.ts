/** Labeled percentage meter for augmentation / ship attribute bars (Equipment §2). */
export interface StatMeterProps {
  label?: string;
  value?: number; // 0–100
  color?: string;
  showValue?: boolean;
  width?: number | string;
  style?: React.CSSProperties;
}
export declare function StatMeter(props: StatMeterProps): JSX.Element;
/** Insertable mod chip / augmentation: rarity-framed icon + name + effect line (Equipment §4). */
export interface ModChipProps {
  name?: string;
  effect?: string;
  rarity?: 'common'|'uncommon'|'rare'|'epic'|'legendary'|'mythic'|'exotic';
  icon?: string;
  image?: string;
  style?: React.CSSProperties;
}
export declare function ModChip(props: ModChipProps): JSX.Element;
