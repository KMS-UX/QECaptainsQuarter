/** Full-screen loading / transition card: gold gradient wordmark, glowing progress bar, percent readout, optional tips panel. */
export interface LoadingScreenProps {
  title?: string;
  progress?: number; // 0–100
  status?: string;
  tip?: string;
  width?: number;
  style?: React.CSSProperties;
}
export declare function LoadingScreen(props: LoadingScreenProps): JSX.Element;
