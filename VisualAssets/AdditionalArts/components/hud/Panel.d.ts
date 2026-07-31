/** Dark HUD panel: scanline texture, 1px border, corner tick accents, optional caps title in the accent color. */
export interface PanelProps {
  title?: string;
  accent?: string;
  corners?: boolean;
  children?: React.ReactNode;
  style?: React.CSSProperties;
}
export declare function Panel(props: PanelProps): JSX.Element;
