/** Gradient resource bar on dark track. Kinds: hp (red→orange), ep (blue→cyan), xp (gold), boss (purple). */
export interface StatBarProps {
  kind?: 'hp' | 'ep' | 'xp' | 'boss';
  value?: number;
  max?: number;
  label?: string;
  showValue?: boolean;
  height?: number;
  style?: React.CSSProperties;
}
export declare function StatBar(props: StatBarProps): JSX.Element;
