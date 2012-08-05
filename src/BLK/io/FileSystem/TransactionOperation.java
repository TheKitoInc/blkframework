/*
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 */

package BLK.io.FileSystem;

import BLK.io.FileSystem.Transaction.operations;
import BLK.io.FileSystem.Transaction.status;

/**
 *
 * @author The Blankis < blankitoracing@gmail.com >
 */
public class TransactionOperation {
     @Override
        public boolean equals(Object obj)
     {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TransactionOperation other = (TransactionOperation) obj;
            if (this.o != other.o) {
                return false;
            }
            if (this.source != other.source && (this.source == null || !this.source.equals(other.source))) {
                return false;
            }
            if (this.destination != other.destination && (this.destination == null || !this.destination.equals(other.destination))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 11 * hash + this.o.hashCode();
            hash = 11 * hash + (this.source != null ? this.source.hashCode() : 0);
            hash = 11 * hash + (this.destination != null ? this.destination.hashCode() : 0);
            return hash;
        }

        private operations o;
        

        private FileSystem source;
        private FileSystem destination;
        private status sta=status.onTail;

        public status getStatus() {
            return sta;
        }

        public void setStatus(status sta) {
            this.sta = sta;
        }


        public FileSystem getDestination() {
            return destination;
        }

        public void setDestination(FileSystem destination) {
            this.destination = destination;
        }

        public operations getO() {
            return o;
        }

        public void setO(operations o) {
            this.o = o;
        }

        public FileSystem getSource() {
            return source;
        }

        public void setSource(FileSystem source) {
            this.source = source;
        }

        public TransactionOperation(operations o, FileSystem source, FileSystem destination)
        {
            this.o = o;
            this.source = source;
            this.destination = destination;
        }
        @Override
        public String toString()
        {
            String os="NULL";
            String ss="NULL";
            String ds="NULL";

            if (this.o!=null)
                os=this.o.toString();

            if (this.source!=null)
                ss=this.source.getPath();

            if (this.destination!=null)
                ds=this.destination.getPath();

            return os + " | " + ss + " | " + ds;

        }

}
