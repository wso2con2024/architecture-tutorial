declare global {
  interface Window {
    configs: {
      apiUrl: string;
      catalogUrl: string;
    };
  }
}

export const apiUrl = window?.configs?.apiUrl ? window.configs.apiUrl : "http://localhost:3000/api/reservations";
export const catalogUrl = window?.configs?.catalogUrl ? window.configs.catalogUrl : "http://localhost:9090";
