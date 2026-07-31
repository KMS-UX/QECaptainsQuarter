/** Rectangular HUD button. Variants from the HUD & UI Kit sheet: primary (PLAY), secondary (EQUIP), ghost (INFO), danger (DELETE). */
export interface ButtonProps {
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  disabled?: boolean;
  onClick?: () => void;
  children?: React.ReactNode;
  style?: React.CSSProperties;
}
export declare function Button(props: ButtonProps): JSX.Element;
