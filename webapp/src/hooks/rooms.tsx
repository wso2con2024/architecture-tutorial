// import { useState } from "react";
// import { AxiosResponse } from "axios";
// import { RoomType } from "../types/generated";
// import { performRequestWithRetry } from "../api/retry";
// import { apiUrl } from "../api/config";





// export function useGetRooms() {
//   const [rooms, setRooms] = useState<RoomType[]>([]);
//   const [loading, setLoading] = useState(false);
//   const [error, setError] = useState<Error>();

//   const fetchRooms = async (
//     checkIn: string,
//     checkOut: string,
//     guestCapacity: number,
//   ): Promise<void> => {
//     setLoading(true);
//     const options = {
//       method: "GET",
//       params: {
//         checkinDate: checkIn,
//         checkoutDate: checkOut,
//         guestCapacity
//       },
//     };
//     try {
//       const response = await performRequestWithRetry(
//         `${apiUrl}/roomTypes`,
//         options
//       );
//       const roomList = (response as AxiosResponse<RoomType[]>).data;
//       setRooms(roomList);
//     } catch (e: any) {
//       setError(e);
//     }
//     setLoading(false);
//   };

//   return { rooms, loading, error, fetchRooms };
// }

//graphQL interface
import { useState } from "react";
import axios, { AxiosResponse } from "axios";
import { RoomType } from "../types/generated";
import { apiUrl, catalogUrl } from "../api/config";

export function useGetRooms() {
  const [rooms, setRooms] = useState<RoomType[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error>();

  const fetchRooms = async (
    checkIn: string,
    checkOut: string,
    guestCapacity: number,
  ): Promise<void> => {
    setLoading(true);
    const query = `
      query MyQuery($checkIn: String!, $checkOut: String!, $guestCapacity: Int!) {
        roomTypes(checkinDate: $checkIn, checkoutDate: $checkOut, guestCapacity: $guestCapacity) {
          guestCapacity
          id
          name
          price
        }
      }
    `;
    const variables = {
      checkIn,
      checkOut,
      guestCapacity,
    };
    try {
      const response: AxiosResponse = await axios({
        url: `${catalogUrl}/roomgql`,
        method: 'POST',
        data: {
          query,
          variables
        },
        headers: {
          'Content-Type': 'application/json',
        },
      });
      const roomList = response.data.data.roomTypes as RoomType[];
      setRooms(roomList);
    } catch (e: any) {
      setError(e);
    } finally {
      setLoading(false);
    }
  };

  return { rooms, loading, error, fetchRooms };
}